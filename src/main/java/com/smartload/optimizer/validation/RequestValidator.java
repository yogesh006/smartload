package com.smartload.optimizer.validation;

import com.smartload.optimizer.model.Order;
import com.smartload.optimizer.model.Truck;

import java.util.List;

public final class RequestValidator {

    private RequestValidator() {}

    public static void validate(Truck truck, List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return; // empty set is allowed, returns zero solution
        }
        // Ensure each order has valid dates
        for (Order o : orders) {
            if (o.getPickupDate().isAfter(o.getDeliveryDate())) {
                throw new IllegalArgumentException("pickup_date must be <= delivery_date for order " + o.getId());
            }
        }
    }

    public static boolean isSameRoute(List<Order> orders) {
        if (orders.isEmpty()) return true;
        String origin = orders.get(0).getOrigin();
        String dest = orders.get(0).getDestination();
        for (Order o : orders) {
            if (!origin.equals(o.getOrigin()) || !dest.equals(o.getDestination())) return false;
        }
        return true;
    }

    public static boolean isHazmatHomogeneous(List<Order> orders) {
        if (orders.isEmpty()) return true;
        boolean hz = orders.get(0).isHazmat();
        for (Order o : orders) {
            if (o.isHazmat() != hz) return false;
        }
        return true;
    }
}
