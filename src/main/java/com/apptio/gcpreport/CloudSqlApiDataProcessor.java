package com.apptio.gcpreport;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.auth.Credentials;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Resources
 * https://github.com/google/google-api-java-client-samples/tree/master/oauth2-cmdline-sample
 * https://www.codota.com/web/assistant/code/rs/5c658dd51095a500015ff220#L551
 * https://cloud.google.com/docs/authentication/end-user
 * https://cloud.google.com/sql/docs/mysql/admin-api
 * https://cloud.google.com/sql/docs/mysql/admin-api/rest/v1beta4/instances/list
 */
public class CloudSqlApiDataProcessor {

  private static final java.io.File DATA_STORE_DIR =
      new java.io.File(System.getProperty("user.home"), ".store/oauth2_sample");
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static HttpTransport httpTransport;
  private static FileDataStoreFactory dataStoreFactory;
  private static final List<String> SCOPES = Arrays.asList(
      "https://www.googleapis.com/auth/userinfo.profile",
      "https://www.googleapis.com/auth/userinfo.email");
  /*
    These scope should be added in the list after the same permission is granted from SQL resource
    https://www.googleapis.com/auth/cloud-platform
    https://www.googleapis.com/auth/sqlservice.admin
   */

  private static Oauth2 oauth2;
  private static final String APPLICATION_NAME = "WebClient";
  private static final String PROJECT_ID = "superb-acumen-287513";
  private static GoogleClientSecrets clientSecrets;

  public static void getSQLData(Credentials myCredentials, String myEndpoint)
  {
    try {
      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
      JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
      // authorization
      /*
      This code promt to "Please open the following address in your browser: .....* for the GCP account log
      in credentials are required for which /src/main/resource/client_secrets.json file need to be update. Hence commenting the code for now.

      Credential credential = authorize();
      // set up global Oauth2 instance
      oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
          APPLICATION_NAME).build();
      // run commands
      System.out.println("Access token:" + credential.getAccessToken());
      SQLAdmin.Builder build = new SQLAdmin.Builder(httpTransport, jsonFactory, credential);
      SQLAdmin sqlAdmin = build.setApplicationName(PROJECT_ID).build();
      InstancesListResponse resp = sqlAdmin.instances().list(PROJECT_ID).execute();
      List<DatabaseInstance> list = resp.getItems();
      if(list!=null){
        for (DatabaseInstance d : list) {
          System.out.println("DatabaseInstance:" + d);
        }
      }
      tokenInfo(credential.getAccessToken());
      userInfo();
      // success!
       */
      return;
    } catch (Exception e) {
      System.err.println(e.getMessage());
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }

  /** Authorizes the installed application to access user's protected data. */
  private static Credential authorize() throws Exception {
    // load client secrets
    clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
        new InputStreamReader(CloudSqlApiDataProcessor.class.getResourceAsStream("/client_secrets.json")));
    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
      System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/ "
          + "into oauth2-cmdline-sample/src/main/resources/client_secrets.json");
      System.exit(1);
    }
    // set up authorization code flow
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        httpTransport, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(
        dataStoreFactory).build();

    LocalServerReceiver localReceiver = new LocalServerReceiver.Builder()
        .setPort(8080).build();
    // authorize
    return new AuthorizationCodeInstalledApp(flow, localReceiver, new AuthorizationCodeInstalledApp.DefaultBrowser()).authorize("user");
  }

  private static void tokenInfo(String accessToken) throws IOException {
    header("Validating a token");
    Tokeninfo tokeninfo = oauth2.tokeninfo().setAccessToken(accessToken).execute();
    System.out.println(tokeninfo.toPrettyString());
    if (!tokeninfo.getAudience().equals(clientSecrets.getDetails().getClientId())) {
      System.err.println("ERROR: audience does not match our client ID!");
    }
  }

  private static void userInfo() throws IOException {
    header("Obtaining User Profile Information");
    Userinfoplus userinfo = oauth2.userinfo().get().execute();
    System.out.println(userinfo.toPrettyString());
  }

  static void header(String name) {
    System.out.println();
    System.out.println("================== " + name + " ==================");
    System.out.println();
  }
}
