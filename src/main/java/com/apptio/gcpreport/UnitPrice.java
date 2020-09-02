package com.apptio.gcpreport;

public class UnitPrice {
  private String currencyCode;
  private String units;
  private long nanos;

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public String getUnits() {
    return units;
  }

  public void setUnits(String units) {
    this.units = units;
  }

  public long getNanos() {
    return nanos;
  }

  public void setNanos(long nanos) {
    this.nanos = nanos;
  }
}
