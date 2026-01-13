package com.smartload.optimizer.model;

import java.math.BigDecimal;
import java.util.List;

public class OptimizeResponse {
    private String truckId;
    private List<String> selectedOrderIds;
    private long totalPayoutCents;
    private int totalWeightLbs;
    private int totalVolumeCuft;
    private BigDecimal utilizationWeightPercent;
    private BigDecimal utilizationVolumePercent;

    public String getTruckId() { return truckId; }
    public List<String> getSelectedOrderIds() { return selectedOrderIds; }
    public long getTotalPayoutCents() { return totalPayoutCents; }
    public int getTotalWeightLbs() { return totalWeightLbs; }
    public int getTotalVolumeCuft() { return totalVolumeCuft; }
    public BigDecimal getUtilizationWeightPercent() { return utilizationWeightPercent; }
    public BigDecimal getUtilizationVolumePercent() { return utilizationVolumePercent; }

    public void setTruckId(String truckId) { this.truckId = truckId; }
    public void setSelectedOrderIds(List<String> selectedOrderIds) { this.selectedOrderIds = selectedOrderIds; }
    public void setTotalPayoutCents(long totalPayoutCents) { this.totalPayoutCents = totalPayoutCents; }
    public void setTotalWeightLbs(int totalWeightLbs) { this.totalWeightLbs = totalWeightLbs; }
    public void setTotalVolumeCuft(int totalVolumeCuft) { this.totalVolumeCuft = totalVolumeCuft; }
    public void setUtilizationWeightPercent(BigDecimal utilizationWeightPercent) { this.utilizationWeightPercent = utilizationWeightPercent; }
    public void setUtilizationVolumePercent(BigDecimal utilizationVolumePercent) { this.utilizationVolumePercent = utilizationVolumePercent; }
}
