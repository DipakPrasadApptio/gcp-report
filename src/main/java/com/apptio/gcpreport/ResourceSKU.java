package com.apptio.gcpreport;


import java.util.List;
import java.util.Map;

public class ResourceSKU {
  public  String resourceType;
  public  String name;
  public  String tier;
  public  String size;
  public  String family;
  public  List<String> locations;
  private List<ResourceSKULocationInfo> locationInfo;
  public Map<String, String> capabilities;
  public List<ResourceSKURestriction> restrictions;

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getFamily() {
    return family;
  }

  public void setFamily(String family) {
    this.family = family;
  }

  public List<String> getLocations() {
    return locations;
  }

  public void setLocations(List<String> locations) {
    this.locations = locations;
  }

  public List<ResourceSKULocationInfo> getLocationInfo() {
    return locationInfo;
  }

  public void setLocationInfo(List<ResourceSKULocationInfo> locationInfo) {
    this.locationInfo = locationInfo;
  }

  public Map<String, String> getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(Map<String, String> capabilities) {
    this.capabilities = capabilities;
  }

  public List<ResourceSKURestriction> getRestrictions() {
    return restrictions;
  }

  public void setRestrictions(List<ResourceSKURestriction> restrictions) {
    this.restrictions = restrictions;
  }
}
