package com.apptio.gcpreport;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.services.compute.ComputeScopes;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.compute.v1.AddressSettings;
import com.google.cloud.compute.v1.MachineType;
import com.google.cloud.compute.v1.MachineTypeClient;
import com.google.cloud.compute.v1.MachineTypeSettings;
import com.google.cloud.compute.v1.ProjectZoneName;
import com.google.cloud.compute.v1.Region;
import com.google.cloud.compute.v1.RegionClient;
import com.google.cloud.compute.v1.RegionSettings;
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

		for(MachineType machineType:machineTypeClient.listMachineTypes(ProjectZoneName.newBuilder()
				.setProject("magnartes")
				.setZone("us-east1-b").build())
				.iterateAll()){
			System.out.println(machineType);
		}

		System.out.println("====================MachineTypes(\"us-east4-a\")======================");

		for(MachineType machineType:machineTypeClient.listMachineTypes(ProjectZoneName.newBuilder()
				.setProject("magnartes")
				.setZone("us-east4-a").build())
				.iterateAll()){
			System.out.println(machineType);
		}
	}



}
