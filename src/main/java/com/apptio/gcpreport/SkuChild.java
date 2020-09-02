package com.apptio.gcpreport;

public class SkuChild {
  private String name;
  private String skuId;
  private String description;
  private Category category;
  private String[] serviceRegions;
  private PricingInfo[] pricingInfo;
  private String serviceProviderName;
  private GeoTaxonomy geoTaxonomy;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSkuId() {
    return skuId;
  }

  public void setSkuId(String skuId) {
    this.skuId = skuId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public String[] getServiceRegions() {
    return serviceRegions;
  }

  public void setServiceRegions(String[] serviceRegions) {
    this.serviceRegions = serviceRegions;
  }

  public String getServiceProviderName() {
    return serviceProviderName;
  }

  public void setServiceProviderName(String serviceProviderName) {
    this.serviceProviderName = serviceProviderName;
  }

  public PricingInfo[] getPricingInfo() {
    return pricingInfo;
  }

  public void setPricingInfo(PricingInfo[] pricingInfo) {
    this.pricingInfo = pricingInfo;
  }

  public GeoTaxonomy getGeoTaxonomy() {
    return geoTaxonomy;
  }

  public void setGeoTaxonomy(GeoTaxonomy geoTaxonomy) {
    this.geoTaxonomy = geoTaxonomy;
  }
}
