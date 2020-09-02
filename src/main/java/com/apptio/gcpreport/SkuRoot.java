package com.apptio.gcpreport;

public class SkuRoot {
  private SkuChild[] skus;
  private String nextPageToken;

  public SkuChild[] getSkus() {
    return skus;
  }

  public void setSkus(SkuChild[] skus) {
    this.skus = skus;
  }

  public String getNextPageToken() {
    return nextPageToken;
  }

  public void setNextPageToken(String nextPageToken) {
    this.nextPageToken = nextPageToken;
  }
}

