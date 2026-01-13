package com.smartload.optimizer.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class OptimizeRequest {
    @Valid @NotNull
    private Truck truck;

    @Valid @NotNull
    private List<Order> orders;

    public Truck getTruck() { return truck; }
    public List<Order> getOrders() { return orders; }

    public void setTruck(Truck truck) { this.truck = truck; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
}
