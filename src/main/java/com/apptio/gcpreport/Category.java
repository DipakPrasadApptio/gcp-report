package com.apptio.gcpreport;

public class Category {
  private String serviceDisplayName;
  private String resourceFamily;
  private String resourceGroup;
  private String usageType;

  public String getServiceDisplayName() {
    return serviceDisplayName;
  }

  public void setServiceDisplayName(String serviceDisplayName) {
    this.serviceDisplayName = serviceDisplayName;
  }

  public String getResourceFamily() {
    return resourceFamily;
  }

  public void setResourceFamily(String resourceFamily) {
    this.resourceFamily = resourceFamily;
  }

  public String getResourceGroup() {
    return resourceGroup;
  }

  public void setResourceGroup(String resourceGroup) {
    this.resourceGroup = resourceGroup;
  }

  public String getUsageType() {
    return usageType;
  }

  public void setUsageType(String usageType) {
    this.usageType = usageType;
  }
}
