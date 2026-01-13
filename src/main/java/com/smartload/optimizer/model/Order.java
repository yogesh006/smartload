package com.smartload.optimizer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class Order {
    @NotBlank
    private String id;
    @Min(0)
    private long payoutCents;
    @Min(1)
    private int weightLbs;
    @Min(1)
    private int volumeCuft;
    @NotBlank
    private String origin;
    @NotBlank
    private String destination;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickupDate;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;
    private boolean hazmat;

    public String getId() { return id; }
    public long getPayoutCents() { return payoutCents; }
    public int getWeightLbs() { return weightLbs; }
    public int getVolumeCuft() { return volumeCuft; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDate getPickupDate() { return pickupDate; }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public boolean isHazmat() { return hazmat; }

    public void setId(String id) { this.id = id; }
    public void setPayoutCents(long payoutCents) { this.payoutCents = payoutCents; }
    public void setWeightLbs(int weightLbs) { this.weightLbs = weightLbs; }
    public void setVolumeCuft(int volumeCuft) { this.volumeCuft = volumeCuft; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }
    public void setHazmat(boolean hazmat) { this.hazmat = hazmat; }
}
