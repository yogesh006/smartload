package com.smartload.optimizer.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Truck {
    @NotBlank
    private String id;
    @Min(1)
    private int maxWeightLbs;
    @Min(1)
    private int maxVolumeCuft;

    public String getId() { return id; }
    public int getMaxWeightLbs() { return maxWeightLbs; }
    public int getMaxVolumeCuft() { return maxVolumeCuft; }

    public void setId(String id) { this.id = id; }
    public void setMaxWeightLbs(int maxWeightLbs) { this.maxWeightLbs = maxWeightLbs; }
    public void setMaxVolumeCuft(int maxVolumeCuft) { this.maxVolumeCuft = maxVolumeCuft; }
}
