package com.apptio.gcpreport;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ResourceSKURestriction {

  public  String type;
  public List<String> values;
  public  ResourceSKURestrictionInfo restrictionInfo;
  public  String reasonCode;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public ResourceSKURestrictionInfo getRestrictionInfo() {
    return restrictionInfo;
  }

  public void setRestrictionInfo(ResourceSKURestrictionInfo restrictionInfo) {
    this.restrictionInfo = restrictionInfo;
  }

  public String getReasonCode() {
    return reasonCode;
  }

  public void setReasonCode(String reasonCode) {
    this.reasonCode = reasonCode;
  }
}
