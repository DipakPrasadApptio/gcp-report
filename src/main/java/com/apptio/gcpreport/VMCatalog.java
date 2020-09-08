package com.apptio.gcpreport;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

public class VMCatalog {
  public  String resourceType;
  public  String name;
  public  List<String> terms;
  public  List<String> locations;
  public  Map<String, List<String>> billingPlans;
  public Map<String, String> skuProperties;

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

  public List<String> getTerms() {
    return terms;
  }

  public void setTerms(List<String> terms) {
    this.terms = terms;
  }

  public List<String> getLocations() {
    return locations;
  }

  public void setLocations(List<String> locations) {
    this.locations = locations;
  }

  public Map<String, List<String>> getBillingPlans() {
    return billingPlans;
  }

  public void setBillingPlans(Map<String, List<String>> billingPlans) {
    this.billingPlans = billingPlans;
  }

  public Map<String, String> getSkuProperties() {
    return skuProperties;
  }

  public void setSkuProperties(Map<String, String> skuProperties) {
    this.skuProperties = skuProperties;
  }
}
