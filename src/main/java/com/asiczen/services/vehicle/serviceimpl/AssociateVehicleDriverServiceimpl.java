package com.asiczen.services.vehicle.serviceimpl;

import com.asiczen.services.vehicle.exception.AccessisDeniedException;
import com.asiczen.services.vehicle.exception.InternalServerError;
import com.asiczen.services.vehicle.exception.ResourceNotFoundException;
import com.asiczen.services.vehicle.model.Driver;
import com.asiczen.services.vehicle.model.Vehicle;
import com.asiczen.services.vehicle.repository.DriverRepository;
import com.asiczen.services.vehicle.repository.VehicleRepository;
import com.asiczen.services.vehicle.response.AssociationMessage;
import com.asiczen.services.vehicle.services.AssociateVehicleDriver;
import com.asiczen.services.vehicle.utility.UtilityServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class AssociateVehicleDriverServiceimpl implements AssociateVehicleDriver {

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    UtilityServices utilService;

    @Autowired
    DriverRepository driverRepository;

    @Override
    public AssociationMessage associateVehicleAndDriver(Long vehicleId, Long driverId, String token) {
        String orgRefName = getOrganizationFromToken(token);
        log.trace("Vehicle Driver mapping has started");
        log.trace("Vehicle id {} ", vehicleId);
        log.trace("Driver id {}", driverId);

        Vehicle vehicle = vehicleRepository.findByVehicleIdAndOrgRefName(vehicleId, orgRefName).orElseThrow(() -> new ResourceNotFoundException("Invalid vehicle reference"));
        Driver driver = driverRepository.findByDriverIdAndOrgRefName(driverId, orgRefName).orElseThrow(() -> new ResourceNotFoundException("Invalid driver reference"));

        driver.getVehicles().clear();
        driver.getVehicles().add(vehicle);
        driver.setUpdatedAt(getCurrentTimeStamp());

        try {
            driverRepository.save(driver);
            return new AssociationMessage("Vehicle and Driver linked successfully.");
        } catch (Exception ep) {
            log.error("Error occurred while linking vehicle and driver {} ", ep.getLocalizedMessage());
            throw new InternalServerError("error while linking the vehicle , please contact developer.");
        }
    }

    private Date getCurrentTimeStamp() {
        Date date = new Date();
        return date;
    }

    @Override
    public AssociationMessage disAssociateVehicleAndDriver(Long vehicleId, Long driverId, String token) {
        String orgRefName = getOrganizationFromToken(token);

        Optional<Vehicle> optionalVehicle = vehicleRepository.findByVehicleIdAndOrgRefName(vehicleId, orgRefName);
        optionalVehicle.ifPresentOrElse(this::setDriverNullAndSaveInDB, () -> new ResourceNotFoundException("invalid vehicle id"));

        return new AssociationMessage("Vehicle and Driver de linked successfully.");
    }

    private void setDriverNullAndSaveInDB(Vehicle vehicle) {
        try {
            vehicleRepository.save(vehicle);
        } catch (Exception exception) {
            log.error("Error occurred while linking vehicle and driver {} ", exception.getLocalizedMessage());
            throw new InternalServerError("error while linking the vehicle , please contact developer.");
        }

    }

    private String getOrganizationFromToken(String token) {
        String orgRefName = utilService.getCurrentUserOrgRefName(token);
        return Optional.ofNullable(orgRefName).orElseThrow(() -> new AccessisDeniedException("Access is denied."));
    }
}
