package com.apptio.gcpreport;

//import org.apache.http.client.HttpClient;

import netscape.javascript.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class CloudSqlSkuExample {

  public static void main(String[] args) {
    System.out.println("hello");
    try {
//    HttpClient client = HttpClient.newBuilder()
//        .version(HttpClient.Version.HTTP_1_1)
//        .connectTimeout(Duration.ofSeconds(30))
//        .build();
    HttpRequest request = HttpRequest.newBuilder(new URI("https://cloudbilling.googleapis.com/v1/services/9662-B51E-5089/skus?key=AIzaSyAe04BOXBRAiAyv7jgoCOvnvu-yoax9g28"))
          .header("Accept", "application/json").build();


      SkuRoot response =  HttpClient.newHttpClient().send(request, new JsonBodyHandler<>(SkuRoot.class)).body();
      System.out.println(response.getSkus()[0].getName());
      /**
       *       JSONParser parser = new JSONParser();
       *       JSONObject object = (JSONObject) parser.parse(response.body().toString());
       *       JSONArray skus = (JSONArray)object.get("skus");
       *
       *       System.out.println(skus.get(0));
       */

    } catch (Exception ex)
    {
      System.out.println("Caught exception: " + ex.getMessage());
    }
  }
}
