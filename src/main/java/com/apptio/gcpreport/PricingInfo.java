package com.apptio.gcpreport;

import java.util.Date;

public class PricingInfo {
  private String summary;
  private PricingExpression pricingExpression;
  private int currencyConversionRate;
  private Date effectiveTime;

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public PricingExpression getPricingExpression() {
    return pricingExpression;
  }

  public void setPricingExpression(PricingExpression pricingExpression) {
    this.pricingExpression = pricingExpression;
  }

  public int getCurrencyConversionRate() {
    return currencyConversionRate;
  }

  public void setCurrencyConversionRate(int currencyConversionRate) {
    this.currencyConversionRate = currencyConversionRate;
  }

  public Date getEffectiveTime() {
    return effectiveTime;
  }

  public void setEffectiveTime(Date effectiveTime) {
    this.effectiveTime = effectiveTime;
  }
}
