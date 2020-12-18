package com.asiczen.services.vehicle.services;

import com.asiczen.services.vehicle.request.CreateVehicleRequest;
import com.asiczen.services.vehicle.request.UpdateVehicleRequest;
import com.asiczen.services.vehicle.response.CreateVehicleResponse;
import com.asiczen.services.vehicle.response.UpdateVehicleResponse;
import com.asiczen.services.vehicle.response.VehicleDetailsFromLinkedImei;
import com.asiczen.services.vehicle.response.VehicleListResponse;

import java.util.List;

public interface VehicleServices {

	CreateVehicleResponse postNewVehicle(CreateVehicleRequest vehicleRequest, String token);

	UpdateVehicleResponse updateVehicle(UpdateVehicleRequest request, String token);

	List<VehicleListResponse> getVehicleList(String token);
	
	void deleteVehicle(Long vehicleId,String token);

	long countVehicleByOrg(String token);

	Object generateVehicleInfo(String authorization);

	VehicleDetailsFromLinkedImei getVehicleNumberByDevice(String imei);
}
