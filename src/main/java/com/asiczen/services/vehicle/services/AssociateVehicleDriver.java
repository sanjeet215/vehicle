package com.asiczen.services.vehicle.services;

import com.asiczen.services.vehicle.response.AssociationMessage;
import org.springframework.stereotype.Service;

@Service
public interface AssociateVehicleDriver {

    AssociationMessage associateVehicleAndDriver(Long vehicleId, Long deviceId, String token);

    AssociationMessage disAssociateVehicleAndDriver(Long vehicleId,Long deviceId,String token);

}
