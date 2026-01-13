package com.smartload.optimizer.service;

import com.smartload.optimizer.model.OptimizeRequest;
import com.smartload.optimizer.model.OptimizeResponse;
import com.smartload.optimizer.model.Order;
import com.smartload.optimizer.model.Truck;
import com.smartload.optimizer.util.LruCache;
import com.smartload.optimizer.validation.RequestValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoadOptimizerService {

    private final BitmaskOptimizer optimizer = new BitmaskOptimizer();
    private final LruCache<String, OptimizeResponse> cache = new LruCache<>(64);

    public OptimizeResponse optimize(OptimizeRequest req) {
        Truck truck = req.getTruck();
        List<Order> orders = Optional.ofNullable(req.getOrders()).orElseGet(List::of);

        RequestValidator.validate(truck, orders);

        // Filter orders by simple route compatibility and hazmat homogeneity
        List<List<Order>> cohorts = groupByRouteAndHazmat(orders);

        if (cohorts.isEmpty()) {
            return emptyResponse(truck);
        }

        // Over cohorts, pick the best solution
        OptimizeResponse best = emptyResponse(truck);
        long bestVal = -1;

        for (List<Order> cohort : cohorts) {
            // Additional window compatibility: max pickup ≤ min delivery
            if (!isWindowCompatible(cohort)) continue;

            BitmaskOptimizer.Result r = optimizer.optimize(truck, cohort);
            if (r.payout > bestVal) {
                bestVal = r.payout;
                best = buildResponse(truck, cohort, r);
            }
        }
        // Caching by a stable key of inputs
        String cacheKey = cacheKeyOf(req);
        cache.put(cacheKey, best);
        return best;
    }

    private List<List<Order>> groupByRouteAndHazmat(List<Order> orders) {
        // Group by origin+destination+hazmat
        Map<String, List<Order>> map = new LinkedHashMap<>();
        for (Order o : orders) {
            String key = o.getOrigin() + "||" + o.getDestination() + "||" + (o.isHazmat() ? "HZ" : "NHZ");
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(o);
        }
        return new ArrayList<>(map.values());
    }

    private boolean isWindowCompatible(List<Order> orders) {
        if (orders.isEmpty()) return true;
        // max pickup ≤ min delivery across the set to avoid conflicts
        var maxPickup = orders.stream().map(Order::getPickupDate).max(Comparator.naturalOrder()).orElse(null);
        var minDelivery = orders.stream().map(Order::getDeliveryDate).min(Comparator.naturalOrder()).orElse(null);
        return maxPickup != null && minDelivery != null && !maxPickup.isAfter(minDelivery);
    }

    private OptimizeResponse emptyResponse(Truck truck) {
        OptimizeResponse resp = new OptimizeResponse();
        resp.setTruckId(truck.getId());
        resp.setSelectedOrderIds(Collections.emptyList());
        resp.setTotalPayoutCents(0);
        resp.setTotalWeightLbs(0);
        resp.setTotalVolumeCuft(0);
        resp.setUtilizationWeightPercent(BigDecimal.ZERO);
        resp.setUtilizationVolumePercent(BigDecimal.ZERO);
        return resp;
    }

    private OptimizeResponse buildResponse(Truck truck, List<Order> cohort, BitmaskOptimizer.Result r) {
        OptimizeResponse resp = new OptimizeResponse();
        resp.setTruckId(truck.getId());
        resp.setSelectedOrderIds(optimizer.extractIds(cohort, r.mask));
        resp.setTotalPayoutCents(r.payout);
        resp.setTotalWeightLbs(r.weight);
        resp.setTotalVolumeCuft(r.volume);

        BigDecimal weightPct = percent(r.weight, truck.getMaxWeightLbs());
        BigDecimal volumePct = percent(r.volume, truck.getMaxVolumeCuft());

        resp.setUtilizationWeightPercent(weightPct);
        resp.setUtilizationVolumePercent(volumePct);
        return resp;
    }

    private BigDecimal percent(int used, int capacity) {
        if (capacity == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(used)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(capacity), 2, RoundingMode.HALF_UP);
    }

    private String cacheKeyOf(OptimizeRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append(req.getTruck().getId()).append("|")
                .append(req.getTruck().getMaxWeightLbs()).append("|")
                .append(req.getTruck().getMaxVolumeCuft()).append("|");
        req.getOrders().stream()
                .sorted(Comparator.comparing(Order::getId))
                .forEach(o -> sb.append(o.getId()).append(",")
                        .append(o.getPayoutCents()).append(",")
                        .append(o.getWeightLbs()).append(",")
                        .append(o.getVolumeCuft()).append(",")
                        .append(o.getOrigin()).append(",")
                        .append(o.getDestination()).append(",")
                        .append(o.getPickupDate()).append(",")
                        .append(o.getDeliveryDate()).append(",")
                        .append(o.isHazmat()).append(";"));
        return sb.toString();
    }
}
