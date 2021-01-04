package com.asiczen.services.vehicle.services;

import com.asiczen.services.vehicle.model.Owner;
import com.asiczen.services.vehicle.model.Vehicle;
import org.springframework.stereotype.Service;

@Service
public interface OwnerServices {

    Owner findOwnerByVehicleId(long vehicleId,String token);
}
