package com.apptio.gcpreport;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.cloud.compute.v1.Region;
import com.google.cloud.compute.v1.RegionClient;
import com.google.cloud.compute.v1.RegionSettings;

import java.io.IOException;

public class GCPCatalog {
  public static void displayRegionList(Credentials myCredentials, String myEndpoint) throws IOException {

    RegionSettings regionSettings = RegionSettings.newBuilder()
        .setCredentialsProvider(FixedCredentialsProvider.create(myCredentials))
        .setTransportChannelProvider(RegionSettings.defaultHttpJsonTransportProviderBuilder()
            .setEndpoint(myEndpoint).build())
        .build();
    RegionClient regionClient = RegionClient.create(regionSettings);
    RegionClient.ListRegionsPagedResponse listRegionsPagedResponse = regionClient.listRegions("magnartes");
    System.out.println("==========================RegionList=======================");
    for(Region region:listRegionsPagedResponse.iterateAll()){
      System.out.println(region.getName());
    }

  }
}
