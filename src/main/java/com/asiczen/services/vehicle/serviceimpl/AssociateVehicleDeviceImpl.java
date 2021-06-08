package com.asiczen.services.vehicle.serviceimpl;

import com.asiczen.services.vehicle.exception.InternalServerError;
import com.asiczen.services.vehicle.exception.ResourceNotFoundException;
import com.asiczen.services.vehicle.model.Device;
import com.asiczen.services.vehicle.model.Vehicle;
import com.asiczen.services.vehicle.repository.DeviceRepository;
import com.asiczen.services.vehicle.repository.RedisTransformedMessageRepository;
import com.asiczen.services.vehicle.repository.VehicleRepository;
import com.asiczen.services.vehicle.response.AssociationMessage;
import com.asiczen.services.vehicle.services.AssociateVehicleDevice;
import com.asiczen.services.vehicle.utility.UtilityServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Component
@Slf4j
public class AssociateVehicleDeviceImpl implements AssociateVehicleDevice {

    @Autowired
    UtilityServices utilityServices;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    RedisTransformedMessageRepository redisTransformedMessageRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AssociationMessage associateVehicleAndDevice(Long vehicleId, Long deviceId, String token) {

        String orgRefName = utilityServices.getCurrentUserOrgRefName(token);

        Vehicle vehicle = vehicleRepository.findByVehicleIdAndOrgRefName(vehicleId, orgRefName).orElseThrow(() -> new ResourceNotFoundException("Invalid vehicle id or reference"));
        Device device = deviceRepository.findByDeviceIdAndOrgRefName(deviceId, orgRefName).orElseThrow(() -> new ResourceNotFoundException("Invalid device id or reference"));
        vehicle.setDevice(device);
        try {
            if (device.getVehicle() != null) {
                Vehicle oldVehicle = device.getVehicle();
                oldVehicle.setDevice(null);
                vehicleRepository.save(oldVehicle);

                redisTransformedMessageRepository.deleteVehicleInfoByVehicleNumber(oldVehicle.getVehicleRegnNumber());
            }

            vehicleRepository.save(vehicle);
        } catch (Exception exception) {
            log.error(exception.getLocalizedMessage());
            throw new InternalServerError("Error while linking the devices, connect system administrator to check and fix the issue.");
        }

        return new AssociationMessage("Vehicle and device mapped successfully.");
    }

    @Override
    public AssociationMessage disAssociateVehicleAndDevice(Long vehicleId, Long deviceId, String token) {

        String orgRefName = utilityServices.getCurrentUserOrgRefName(token);
        Vehicle vehicle = vehicleRepository.findByVehicleIdAndOrgRefName(vehicleId, orgRefName).orElseThrow(() -> new ResourceNotFoundException("Invalid vehicle id or reference"));
        vehicle.setDevice(null);
        vehicleRepository.save(vehicle);

        return new AssociationMessage("Vehicle and device de linked successfully.");
    }
}
