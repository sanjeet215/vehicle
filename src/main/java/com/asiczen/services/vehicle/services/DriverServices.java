package com.asiczen.services.vehicle.services;


import com.asiczen.services.vehicle.model.Driver;
import com.asiczen.services.vehicle.request.CreateDriverRequest;
import com.asiczen.services.vehicle.request.UpdateDriverRequest;
import com.asiczen.services.vehicle.response.CreateDriverResponse;
import com.asiczen.services.vehicle.response.DriverListResponse;
import com.asiczen.services.vehicle.response.UpdateDriverResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DriverServices {

	public CreateDriverResponse registerDriver(CreateDriverRequest createDriverRequest, String token);
	
	public UpdateDriverResponse updateDriver(UpdateDriverRequest updateDriverRequest, String token);

	public boolean existsDriverContactNumberAndOrgRefName(String contactNumber,String orgRefName);

	public boolean existsDlNumberAndOrgRefName(String dlNumber,String orgRefName);

	public List<DriverListResponse> getDriverListbyOrg(String token);

	public Long countDriverByOrg(String token);

	public Driver getDriverById(Long driverId, String token);

	public void deleteDriverById(Long driverid, String token);

	public Driver getDriverByVehicleId(long vehicleId,String token);

}
