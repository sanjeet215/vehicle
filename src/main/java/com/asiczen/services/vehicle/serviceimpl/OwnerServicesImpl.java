package com.asiczen.services.vehicle.serviceimpl;

import com.asiczen.services.vehicle.exception.ResourceNotFoundException;
import com.asiczen.services.vehicle.model.Owner;
import com.asiczen.services.vehicle.model.Vehicle;
import com.asiczen.services.vehicle.repository.OwnerRepository;
import com.asiczen.services.vehicle.services.OwnerServices;
import com.asiczen.services.vehicle.services.VehicleServices;
import com.asiczen.services.vehicle.utility.UtilityServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OwnerServicesImpl implements OwnerServices {

    @Autowired
    VehicleServices vehicleServices;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    UtilityServices utilityServices;

    @Override
    public Owner findOwnerByVehicleId(long vehicleId, String token) {

        String orgRefName = utilityServices.getCurrentUserOrgRefName(token);
        Vehicle vehicle = vehicleServices.findVehicleByVehicleId(vehicleId, token);
        return ownerRepository.findByOrgRefNameAndVehicles(orgRefName, vehicle).orElseThrow(() -> new ResourceNotFoundException("No vehicle present for Vehicle id"));
    }
}
