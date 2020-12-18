package com.asiczen.services.vehicle.services;

import com.asiczen.services.vehicle.response.AssociationMessage;
import org.springframework.stereotype.Service;

@Service
public interface AssociateVehicleDevice {

    AssociationMessage associateVehicleAndDevice(Long vehicleId, Long deviceId, String token);

    AssociationMessage disAssociateVehicleAndDevice(Long vehicleId, Long deviceId, String token);
}
