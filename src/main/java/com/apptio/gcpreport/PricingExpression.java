package com.apptio.gcpreport;

public class PricingExpression {
  private String usageUnit;
  private String usageUnitDescription;
  private String baseUnit;
  private String baseUnitDescription;
  private long baseUnitConversionFactor;
  private int displayQuantity;
  private TieredRates[] tieredRates;

  public String getUsageUnit() {
    return usageUnit;
  }

  public void setUsageUnit(String usageUnit) {
    this.usageUnit = usageUnit;
  }

  public String getUsageUnitDescription() {
    return usageUnitDescription;
  }

  public void setUsageUnitDescription(String usageUnitDescription) {
    this.usageUnitDescription = usageUnitDescription;
  }

  public String getBaseUnit() {
    return baseUnit;
  }

  public void setBaseUnit(String baseUnit) {
    this.baseUnit = baseUnit;
  }

  public String getBaseUnitDescription() {
    return baseUnitDescription;
  }

  public void setBaseUnitDescription(String baseUnitDescription) {
    this.baseUnitDescription = baseUnitDescription;
  }

  public long getBaseUnitConversionFactor() {
    return baseUnitConversionFactor;
  }

  public void setBaseUnitConversionFactor(long baseUnitConversionFactor) {
    this.baseUnitConversionFactor = baseUnitConversionFactor;
  }

  public int getDisplayQuantity() {
    return displayQuantity;
  }

  public void setDisplayQuantity(int displayQuantity) {
    this.displayQuantity = displayQuantity;
  }

  public TieredRates[] getTieredRates() {
    return tieredRates;
  }

  public void setTieredRates(TieredRates[] tieredRates) {
    this.tieredRates = tieredRates;
  }
}
