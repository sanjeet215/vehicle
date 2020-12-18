package com.asiczen.services.vehicle.services;

import com.asiczen.services.vehicle.request.CreateVehicleRequest;
import com.asiczen.services.vehicle.request.UpdateVehicleRequest;
import com.asiczen.services.vehicle.response.CreateVehicleResponse;
import com.asiczen.services.vehicle.response.UpdateVehicleResponse;
import com.asiczen.services.vehicle.response.VehicleListResponse;

import java.util.List;

public interface VehicleServices {

	public CreateVehicleResponse postNewVehicle(CreateVehicleRequest vehicleRequest, String token);

	public UpdateVehicleResponse updateVehicle(UpdateVehicleRequest request, String token);

	public List<VehicleListResponse> getVehicleList(String token);
	
	public void deleteVehicle(Long vehicleId,String token);

	public long countVehicleByOrg(String token);
}
