package com.apptio.gcpreport;

import java.math.BigDecimal;

public class OnDemandVMPrice {
  public  String meterId;
  public  String meterName;
  public  String region;
  public  String tier;
  public  String size;
  public  BigDecimal consumptionPrice;
  public  BigDecimal reservationPrice;
  public  String currencyCode;
  public  String vcpu;
  public  Long osDiskSizeInMB;
  public  Long resourceDiskSizeInMB;
  public  String memory;
  public  Integer maxDataDiskCount;

  public String getMeterId() {
    return meterId;
  }

  public void setMeterId(String meterId) {
    this.meterId = meterId;
  }

  public String getMeterName() {
    return meterName;
  }

  public void setMeterName(String meterName) {
    this.meterName = meterName;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getTier() {
    return tier;
  }

  public void setTier(String tier) {
    this.tier = tier;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public BigDecimal getConsumptionPrice() {
    return consumptionPrice;
  }

  public void setConsumptionPrice(BigDecimal consumptionPrice) {
    this.consumptionPrice = consumptionPrice;
  }

  public BigDecimal getReservationPrice() {
    return reservationPrice;
  }

  public void setReservationPrice(BigDecimal reservationPrice) {
    this.reservationPrice = reservationPrice;
  }

  public String getVcpu() {
    return vcpu;
  }

  public void setVcpu(String vcpu) {
    this.vcpu = vcpu;
  }

  public Long getOsDiskSizeInMB() {
    return osDiskSizeInMB;
  }

  public void setOsDiskSizeInMB(Long osDiskSizeInMB) {
    this.osDiskSizeInMB = osDiskSizeInMB;
  }

  public Long getResourceDiskSizeInMB() {
    return resourceDiskSizeInMB;
  }

  public void setResourceDiskSizeInMB(Long resourceDiskSizeInMB) {
    this.resourceDiskSizeInMB = resourceDiskSizeInMB;
  }

  public String getMemory() {
    return memory;
  }

  public void setMemory(String memory) {
    this.memory = memory;
  }

  public Integer getMaxDataDiskCount() {
    return maxDataDiskCount;
  }

  public void setMaxDataDiskCount(Integer maxDataDiskCount) {
    this.maxDataDiskCount = maxDataDiskCount;
  }
}
