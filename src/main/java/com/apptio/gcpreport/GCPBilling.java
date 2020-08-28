package com.apptio.gcpreport;

import com.google.cloud.billing.v1.CloudCatalogClient;
import com.google.cloud.billing.v1.CloudCatalogSettings;
import com.google.cloud.billing.v1.ListSkusRequest;
import com.google.cloud.billing.v1.Service;
import com.google.cloud.billing.v1.ServiceName;
import com.google.cloud.billing.v1.Sku;
import org.threeten.bp.Duration;

import java.io.IOException;

public class GCPBilling {
  public static void billingInfo(){
    CloudCatalogSettings.Builder cloudCatalogSettingsBuilder =
        CloudCatalogSettings.newBuilder();
    cloudCatalogSettingsBuilder
        .listServicesSettings()
        .setRetrySettings(
            cloudCatalogSettingsBuilder.listServicesSettings().getRetrySettings().toBuilder()
                .setTotalTimeout(Duration.ofSeconds(30))
                .build());

    try (CloudCatalogClient cloudCatalogClient = CloudCatalogClient.create()) {

      int serviceCount = 0;
      for (Service element : cloudCatalogClient.listServices().iterateAll()) {
        System.out.println(element);
        serviceCount++;
      }
      System.out.println("Service count = " + serviceCount);

      ServiceName parent = ServiceName.of("6F81-5844-456A");


      System.out.println("Getting public SKU List\n==============================================\n==============================================");
      int skuCount = 0;

      String token = "";
      do {
        ListSkusRequest request = ListSkusRequest.newBuilder()
            .setParent(parent.toString())
            .setPageToken(token)
            .build();
        CloudCatalogClient.ListSkusPagedResponse listSkusPagedResponse = cloudCatalogClient.listSkus(request);
        token = listSkusPagedResponse.getNextPageToken();
        System.out.println(token);
        request.toBuilder().setPageToken(token).build();
        for (Sku element : listSkusPagedResponse.iterateAll()) {
          skuCount++;
        }
      } while (!token.equals(""));
      System.out.println("Total Sku = " + skuCount);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
