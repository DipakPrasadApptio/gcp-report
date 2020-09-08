package com.apptio.gcpreport;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.services.compute.ComputeScopes;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.compute.v1.AddressSettings;
import com.google.cloud.compute.v1.MachineTypeClient;
import com.google.cloud.compute.v1.MachineTypeSettings;
import com.google.cloud.compute.v1.Zone;
import com.google.cloud.compute.v1.ZoneClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GcpReportApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(GcpReportApplication.class, args);

		Credentials myCredentials = GoogleCredentials.getApplicationDefault().createScoped(ComputeScopes.all());

		String myEndpoint = AddressSettings.getDefaultEndpoint();
		GCPCatalog.displayRegionList(myCredentials,myEndpoint);

		System.out.println("====================ZoneList======================");

		for(Zone zone: ZoneClient.create().listZones("magnartes").iterateAll()){
			System.out.println(zone);
		}

		MachineTypeSettings machineTypeSettings = MachineTypeSettings.newBuilder()
				.setCredentialsProvider(FixedCredentialsProvider.create(myCredentials))
				.setTransportChannelProvider(MachineTypeSettings.defaultHttpJsonTransportProviderBuilder()
						.setEndpoint(myEndpoint).build())
				.build();
		MachineTypeClient machineTypeClient = MachineTypeClient.create(machineTypeSettings);

		System.out.println("====================MachineTypes(\"us-east1-b\")======================");

		CloudSqlApiDataProcessor.getSQLData(myCredentials, myEndpoint);
	}



}
