package com.apptio.gcpreport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.json.simple.JSONArray;
import com.google.common.base.Strings;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
//import org.json.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class AzureDataProcessor {

  private static List<RetailItemModel> unmatchedRetail = new ArrayList<>();
  static final String SERVICE_FAMILY = "Compute";
  static final String SERVICE_NAME = "Virtual Machines";
  static final String CONSUMPTION_TYPE = "Consumption";
  static final String RESERVATION_TYPE = "Reservation";
  private static String ONDEMAND_MATCHED_METERID_PRICE = "/Users/hhussain/Documents/codebase/gcp-report/src/main/resources/OnDemandVMPrice.csv";
  private static String ONDEMAND_UNMATCHED_METERID_PRICE = "/Users/hhussain/Documents/codebase/gcp-report/src/main/resources/OnDemandUnMatchedVMPrice.csv";

  public static void main(String[] args) {
    System.out.println("AzureDataProcessor");
    StringBuilder stringBuilder = new StringBuilder();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    JSONParser parser = new JSONParser();



      /*
        Convert CSV to RetailItemModel object
       */
    Map<String, String> mapping = new HashMap<>();
    mapping.put("currencyCode", "currencyCode");
    mapping.put("tierMinimumUnits", "tierMinimumUnits");
    mapping.put("retailPrice", "retailPrice");
    mapping.put("unitPrice", "unitPrice");
    mapping.put("armRegionName", "armRegionName");
    mapping.put("location", "location");
    mapping.put("effectiveStartDate", "effectiveStartDate");
    mapping.put("meterId", "meterId");
    mapping.put("meterName", "meterName");
    mapping.put("productId", "productId");
    mapping.put("skuId", "skuId");
    mapping.put("productName", "productName");
    mapping.put("skuName", "skuName");
    mapping.put("serviceName", "serviceName");
    mapping.put("serviceId", "serviceId");
    mapping.put("serviceFamily", "serviceFamily");
    mapping.put("unitOfMeasure", "unitOfMeasure");
    mapping.put("type", "type");
    mapping.put("isPrimaryMeterRegion", "isPrimaryMeterRegion");
    mapping.put("armSkuName", "armSkuName");
    HeaderColumnNameTranslateMappingStrategy<RetailItemModel> strategy =
        new HeaderColumnNameTranslateMappingStrategy<>();
    strategy.setType(RetailItemModel.class);
    strategy.setColumnMapping(mapping);
    List<RetailItemModel> retailItemModels = new ArrayList<>();
    try {
      CSVReader csvReader = new CSVReader(new FileReader("/Users/hhussain/Documents/codebase/gcp-report/src/main/resources/retail_price.csv"));
      CsvToBean csvToBean = new CsvToBean<>();
      csvToBean.setCsvReader(csvReader);
      csvToBean.setMappingStrategy(strategy);
      retailItemModels = csvToBean.parse();

      // Filter data by service family, service name, type
      List<RetailItemModel> filteredRetailPrice = retailItemModels.stream()
          .filter(retail -> SERVICE_FAMILY.equals(retail.getServiceFamily()))
          .filter(retail -> SERVICE_NAME.equals(retail.getServiceName()))
          .filter(retail -> CONSUMPTION_TYPE.equals(retail.getType()) || RESERVATION_TYPE.equals(retail.getType()))
          .collect(Collectors.toList());

      Map<String, List<RetailItemModel>> retailItemModelMap = filteredRetailPrice.
          stream().collect(Collectors.groupingBy(model -> model.getMeterId()));

      System.out.println("Finished reading Retail data CSV, unique counted to: "
          + retailItemModelMap.size() + " , total record: " + retailItemModels.size());

      /*
        Fetch SKU data
       */
      Reader skuReader = new FileReader("/Users/hhussain/Downloads/sku.json");

      JSONArray skuData = (JSONArray) parser.parse(skuReader);
      List<ResourceSKU> skuStore = new ArrayList<>();

      for (Object obj : skuData) {
        skuStore.add(objectMapper.readValue(obj.toString(), ResourceSKU.class));
      }

      Map<String, ResourceSKU> skuByNameAndLocation = skuStore.stream()
          .filter(s -> "virtualMachines".equals(s.resourceType))
          .collect(Collectors.toMap(c -> getSkuKey(c.name, c.locations, c.size), c -> c));

      skuReader.close();

      System.out.println("Finished reading SKU data JSON, counted to: " + skuByNameAndLocation.size());

      /*
        Fetch catalog data
       */
      Reader catalogReader = new FileReader("/Users/hhussain/Downloads/catalog_latest.json");
      JSONArray catalogData = (JSONArray) parser.parse(catalogReader);
      List<VMCatalog> catalogStore = new ArrayList<>();

      for (Object obj : catalogData) {
        catalogStore.add(objectMapper.readValue(obj.toString(), VMCatalog.class));
      }

      Map<String, List<VMCatalog>> catalogByMeterId = catalogStore.stream()
          .collect(Collectors.groupingBy(c -> c.skuProperties.get("MeterId")));

      catalogReader.close();

      System.out.println("Finished reading Catalog data JSON, unique counted to: "
          + catalogByMeterId.size() + " , total record: " + catalogStore.size());

      /*
        Prepare price-sheet data
       */
      List<OnDemandVMPrice> onDemandVMPriceList = new ArrayList<>();

      for(String retailMeterId : retailItemModelMap.keySet())
      {
        if(catalogByMeterId.containsKey(retailMeterId))
        {
//          List<RetailItemModel> consumptionRetailPrice = retailItemModelMap.get(retailMeterId).stream()
//              .filter(r -> CONSUMPTION_TYPE.equals(r.getType())).collect(Collectors.toList());
//
//          List<RetailItemModel> reservationRetailPrice = retailItemModelMap.get(retailMeterId).stream()
//              .filter(r -> RESERVATION_TYPE.equals(r.getType())).collect(Collectors.toList());
//
//          double unitConsumptionPrice = consumptionRetailPrice.stream().max(Comparator.comparing(RetailItemModel::getEffectiveStartDate)).get().getUnitPrice();
//          double unitReservationPrice = reservationRetailPrice.stream().max(Comparator.comparing(RetailItemModel::getEffectiveStartDate)).get().getUnitPrice();
//
//          String[] unitOfMeasure = retailItemModelMap.get(retailMeterId).get(0).getUnitOfMeasure().split(" ");
//
          List<VMCatalog> catalogs = catalogByMeterId.get(retailMeterId);
//
          for (VMCatalog catalog : catalogs) {
//            String[] tierAndSize = catalog.name.split("_", 2);
//
//            if (tierAndSize.length != 2) {
//              continue;
//            }
//
//            ResourceSKU resourceSKU =
//                skuByNameAndLocation == null ? null : skuByNameAndLocation.get(
//                    getSkuKey(catalog.name, catalog.locations, tierAndSize[1]));

            OnDemandVMPrice onDemandVMPrice = preparePrice(retailMeterId, retailItemModelMap, skuByNameAndLocation, catalog);
//            onDemandVMPrice.setMeterId(retailMeterId);
//            onDemandVMPrice.setMeterName(retailItemModelMap.get(retailMeterId).get(0).getMeterName());
//            onDemandVMPrice.setRegion(catalog.locations.get(0));
//            onDemandVMPrice.setTier(tierAndSize[0]);
//            onDemandVMPrice.setSize(tierAndSize[1]);
//            onDemandVMPrice.setConsumptionPrice(new BigDecimal(unitConsumptionPrice).divide(new BigDecimal(unitOfMeasure[0]), 6, RoundingMode.HALF_UP));
//            onDemandVMPrice.setReservationPrice(new BigDecimal(unitReservationPrice).divide(new BigDecimal(unitOfMeasure[0]), 6, RoundingMode.HALF_UP));
//
//            onDemandVMPrice.setCurrencyCode(retailItemModelMap.get(retailMeterId).get(0).getCurrencyCode());
//            onDemandVMPrice.setVcpu(getCapabilityString(resourceSKU, "vCPUs", new HashMap<>()));
//            onDemandVMPrice.setOsDiskSizeInMB(getCapabilityLong(resourceSKU, "OSVhdSizeMB"));
//            onDemandVMPrice.setResourceDiskSizeInMB(getCapabilityLong(resourceSKU, "MaxResourceVolumeMB"));
//            onDemandVMPrice.setMemory(getCapabilityMemory(resourceSKU, "MemoryGB"));
//            onDemandVMPrice.setMaxDataDiskCount(getCapabilityInteger(resourceSKU, "MaxDataDiskCount"));

            onDemandVMPriceList.add(onDemandVMPrice);
          }
        } else
        {
          unmatchedRetail.addAll(retailItemModelMap.get(retailMeterId));
        }
      }

      writeIntoCsv(onDemandVMPriceList, ONDEMAND_MATCHED_METERID_PRICE);
      /*
      PROCESSING UNMATCHED RETAIL DATA
       */
      List<OnDemandVMPrice> onDemandVMPriceUnmatchedList = new ArrayList<>();
      Map<String, VMCatalog> catalogByNameAndRegion = new HashMap<>();

      for(VMCatalog catalog : catalogStore)
      {
        String key = catalog.getName() + catalog.getLocations().get(0);
        catalogByNameAndRegion.put(key, catalog);
        //todo could have map of key to list - to handle multiple records
      }

      List<OnDemandVMPrice> matchedByNameAndRegion = new ArrayList<>();

      Map<String, List<RetailItemModel>> retailByNameAndRegion = new HashMap<>();

      for(RetailItemModel retailItemModel : unmatchedRetail)
      {
        String key = retailItemModel.getArmSkuName() + retailItemModel.getArmRegionName();
        if(retailByNameAndRegion.containsKey(key))
        {
          retailByNameAndRegion.get(key).add(retailItemModel);
        } else
        {
          List<RetailItemModel> addList = new ArrayList<>();
          addList.add(retailItemModel);
          retailByNameAndRegion.put(key, addList);
        }
        //todo could have map of key to list - to handle multiple records
      }

      for(String retailPriceKey : retailByNameAndRegion.keySet())
      {
        if(catalogByNameAndRegion.containsKey(retailPriceKey))
        {
          OnDemandVMPrice price = preparePrice(retailPriceKey,
              retailByNameAndRegion, skuByNameAndLocation, catalogByNameAndRegion.get(retailPriceKey));

          matchedByNameAndRegion.add(price);
        }
      }

      writeIntoCsv(matchedByNameAndRegion, ONDEMAND_UNMATCHED_METERID_PRICE);

      System.out.println("Matched meterId count : " + onDemandVMPriceList.size());
      System.out.println("UnMatched meterId count : " + unmatchedRetail.size());

    } catch (Exception ex)
    {
      System.out.println("Caught exception: " + ex.getMessage());
    }
  }

  private static void writeIntoCsv( List<OnDemandVMPrice> onDemandVMPriceList, String fileName)
  {
    try {
      FileWriter fileWriter = new FileWriter(fileName);
      ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
      mappingStrategy.setType(OnDemandVMPrice.class);

      String[] columns = new String[] {"meterId", "meterName", "region", "tier", "size", "consumptionPrice",
          "reservationPrice", "currencyCode", "vcpu", "osDiskSizeInMB",
          "resourceDiskSizeInMB", "memory", "maxDataDiskCount"};

      mappingStrategy.setColumnMapping(columns);

      fileWriter.append("MeterId, MeterName, Region, Tier, Size, ConsumptionPrice, ReservationPrice, CurrencyCode, " +
          "Vcpu, OsDiskSizeInMB, ResourceDiskSizeInMB, Memory, MaxDataDiskCount");
      StatefulBeanToCsvBuilder<OnDemandVMPrice> builder= new StatefulBeanToCsvBuilder(fileWriter);

      StatefulBeanToCsv beanWriter = builder.withMappingStrategy(mappingStrategy).build();
      beanWriter.write(new OnDemandVMPrice());
      beanWriter.write(onDemandVMPriceList);
      fileWriter.close();
    }  catch (Exception ex)
    {
      System.out.println("Caught exception: " + ex.getMessage());
    }
  }

  private static OnDemandVMPrice preparePrice(String retailMeterId, Map<String,
      List<RetailItemModel>> retailByNameAndRegion, Map<String, ResourceSKU> skuByNameAndLocation, VMCatalog catalog)
  {
    List<RetailItemModel> consumptionRetailPrice = retailByNameAndRegion.get(retailMeterId).stream()
        .filter(r -> CONSUMPTION_TYPE.equals(r.getType())).collect(Collectors.toList());

    List<RetailItemModel> reservationRetailPrice = retailByNameAndRegion.get(retailMeterId).stream()
        .filter(r -> RESERVATION_TYPE.equals(r.getType())).collect(Collectors.toList());

    double unitConsumptionPrice = consumptionRetailPrice.stream().max(Comparator.comparing(RetailItemModel::getEffectiveStartDate)).get().getUnitPrice();
    double unitReservationPrice = reservationRetailPrice.stream().max(Comparator.comparing(RetailItemModel::getEffectiveStartDate)).get().getUnitPrice();

    String[] unitOfMeasure = retailByNameAndRegion.get(retailMeterId).get(0).getUnitOfMeasure().split(" ");


    String[] tierAndSize = catalog.name.split("_", 2);

    if (tierAndSize.length != 2) {
      return new OnDemandVMPrice();
    }

    ResourceSKU resourceSKU =
        skuByNameAndLocation == null ? null : skuByNameAndLocation.get(
            getSkuKey(catalog.name, catalog.locations, tierAndSize[1]));

    OnDemandVMPrice onDemandVMPrice = new OnDemandVMPrice();
    onDemandVMPrice.setMeterId(retailMeterId);
    onDemandVMPrice.setMeterName(retailByNameAndRegion.get(retailMeterId).get(0).getMeterName());
    onDemandVMPrice.setRegion(catalog.locations.get(0));
    onDemandVMPrice.setTier(tierAndSize[0]);
    onDemandVMPrice.setSize(tierAndSize[1]);
    onDemandVMPrice.setConsumptionPrice(new BigDecimal(unitConsumptionPrice).divide(new BigDecimal(unitOfMeasure[0]), 6, RoundingMode.HALF_UP));
    onDemandVMPrice.setReservationPrice(new BigDecimal(unitReservationPrice).divide(new BigDecimal(unitOfMeasure[0]), 6, RoundingMode.HALF_UP));

    onDemandVMPrice.setCurrencyCode(retailByNameAndRegion.get(retailMeterId).get(0).getCurrencyCode());
    onDemandVMPrice.setVcpu(getCapabilityString(resourceSKU, "vCPUs", new HashMap<>()));
    onDemandVMPrice.setOsDiskSizeInMB(getCapabilityLong(resourceSKU, "OSVhdSizeMB"));
    onDemandVMPrice.setResourceDiskSizeInMB(getCapabilityLong(resourceSKU, "MaxResourceVolumeMB"));
    onDemandVMPrice.setMemory(getCapabilityMemory(resourceSKU, "MemoryGB"));
    onDemandVMPrice.setMaxDataDiskCount(getCapabilityInteger(resourceSKU, "MaxDataDiskCount"));

      return onDemandVMPrice;
    }
  public static Integer getCapabilityInteger(ResourceSKU resourceSKU, String name) {
    String value = resourceSKU == null ? null : resourceSKU.capabilities.get(name);
    return (value == null || value.isEmpty()) ? null : Integer.valueOf(value);
  }

  public static String getCapabilityMemory(ResourceSKU resourceSKU, String name) {
    String value = resourceSKU == null ? null : resourceSKU.capabilities.get(name);
    return (value == null || value.isEmpty()) ? null : value + " GiB";
  }

  public static Long getCapabilityLong(ResourceSKU resourceSKU, String name) {
    String value = resourceSKU == null ? null : resourceSKU.capabilities.get(name);
    return (value == null || value.isEmpty()) ? null : Long.valueOf(value);
  }

  public static String getCapabilityString(
      ResourceSKU resourceSKU,
      String name,
      Map<String, String> missingValues
  ) {
    if (resourceSKU == null || resourceSKU.capabilities == null) {
      return null;
    }
    String value = resourceSKU.capabilities.get(name);
    if (Strings.isNullOrEmpty(value)) {
      return missingValues.get(resourceSKU.name);
    }
    return value;
  }

  public static String getSkuKey(String name, List<String> locations, String size) {
    return name + locations.get(0) + size;
  }

  //TODO currently unit of measure is found to be in Hour
  public static boolean isHourOrHours(String unitOfUnitMeasure) {
    Pattern pattern = Pattern.compile("(Hour[s]?|Hrs)");
    Matcher matcher = pattern.matcher(unitOfUnitMeasure);
    return matcher.matches();
  }

  private static void downloadRetail()
  {
     /*
     Download data from Retail end-point into CSV
     */
//    try {
//      HttpRequest request = HttpRequest.newBuilder(new URI("https://prices.azure.com/api/retail/prices"))
//          .header("Accept", "application/json").build();
//
//      // parallel execution  here for each call
//      // Save data in to CSV - 8 cores
//
//      String response =  HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
//
//      JSONObject object = new JSONObject(response);
//      JSONArray jsonArray = object.getJSONArray("Items");
//
//
//      Object link =  object.get("NextPageLink");
//      //assuming first time response is not empty
//      int counter = 1;
//      String name = "";
//      while(!JSONObject.NULL.equals(link))
//      {
//        name = (String) link;
//        System.out.println("Page: " + name);
//
//        request = HttpRequest.newBuilder(new URI(name))
//            .header("Accept", "application/json").build();
//        response =  HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
//
//        if(response != null && !response.isEmpty()) {
//         object = new JSONObject(response);
//          for(Object obj : object.getJSONArray("Items"))
//          {
//            jsonArray.put(obj);
//          }
//
//          link = object.get("NextPageLink");
//          if(!JSONObject.NULL.equals(link)) name = (String) link;
//          counter++;
//        } else
//        {
//          link = null;
//          System.out.println("No more data found");
//        }
//      }
//      File file=new File("/Users/hhussain/Documents/codebase/gcp-report/src/main/resources/retail_price.csv");
//      String csv = CDL.toString(jsonArray);
//      FileUtils.writeStringToFile(file, csv);


//      System.out.println("###################Done fetching all data###################");
  }

}
