package com.smartload.optimizer.service;

import com.smartload.optimizer.model.Order;
import com.smartload.optimizer.model.Truck;

import java.util.ArrayList;
import java.util.List;

public class BitmaskOptimizer {

    public static class Result {
        public final long payout;
        public final int weight;
        public final int volume;
        public final int mask;

        Result(long payout, int weight, int volume, int mask) {
            this.payout = payout;
            this.weight = weight;
            this.volume = volume;
            this.mask = mask;
        }
    }

    public Result optimize(Truck truck, List<Order> compatible) {
        int n = compatible.size();
        if (n == 0) {
            return new Result(0L, 0, 0, 0);
        }
        // Pre-arrays for speed
        long[] value = new long[n];
        int[] w = new int[n];
        int[] v = new int[n];

        for (int i = 0; i < n; i++) {
            Order o = compatible.get(i);
            value[i] = o.getPayoutCents();
            w[i] = o.getWeightLbs();
            v[i] = o.getVolumeCuft();
        }

        // DP over masks with pruning.
        // We cannot do classic 2D knapsack because we want exact subset selection under two constraints,
        // and n ≤ 22 makes bitmask feasible.
        long bestValue = 0;
        int bestMask = 0;
        int bestW = 0, bestV = 0;

        int totalMasks = 1 << n;
        // Simple iterative loop; early prune on cumulative exceed.
        for (int mask = 1; mask < totalMasks; mask++) {
            long sumVal = 0;
            int sumW = 0;
            int sumV = 0;
            // fast iterate bits
            int m = mask;
            int idx = 0;
            while (m != 0) {
                if ((m & 1) == 1) {
                    sumVal += value[idx];
                    sumW += w[idx];
                    sumV += v[idx];
                    if (sumW > truck.getMaxWeightLbs() || sumV > truck.getMaxVolumeCuft()) {
                        sumVal = Long.MIN_VALUE; // prune
                        break;
                    }
                }
                idx++;
                m >>= 1;
            }
            if (sumVal > bestValue) {
                bestValue = sumVal;
                bestMask = mask;
                bestW = sumW;
                bestV = sumV;
            }
        }
        return new Result(bestValue, bestW, bestV, bestMask);
    }

    public List<String> extractIds(List<Order> orders, int mask) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            if ((mask & (1 << i)) != 0) {
                res.add(orders.get(i).getId());
            }
        }
        return res;
    }
}
