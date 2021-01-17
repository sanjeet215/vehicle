package com.asiczen.services.vehicle.serviceimpl;

import com.asiczen.services.vehicle.exception.InternalServerError;
import com.asiczen.services.vehicle.exception.ResourceAlreadyExistException;
import com.asiczen.services.vehicle.exception.ResourceNotFoundException;
import com.asiczen.services.vehicle.model.Device;
import com.asiczen.services.vehicle.model.Vehicle;
import com.asiczen.services.vehicle.repository.DeviceRepository;
import com.asiczen.services.vehicle.repository.RedisTransformedMessageRepository;
import com.asiczen.services.vehicle.repository.RedisVehicleInfoRepository;
import com.asiczen.services.vehicle.repository.VehicleRepository;
import com.asiczen.services.vehicle.request.DeviceRegisterRequest;
import com.asiczen.services.vehicle.request.UpdateDeviceRequest;
import com.asiczen.services.vehicle.response.DeviceListResponse;
import com.asiczen.services.vehicle.response.DeviceResponse;
import com.asiczen.services.vehicle.services.DeviceServices;
import com.asiczen.services.vehicle.utility.UtilityServices;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DeviceServicesImpl implements DeviceServices {

    @Autowired
    DeviceRepository deviceRepo;

    @Autowired
    UtilityServices utilService;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    RedisVehicleInfoRepository redisVehicleInfoRepository;

    @Autowired
    RedisTransformedMessageRepository redisTransformedMessageRepository;

    @Override
    public DeviceResponse registerDevice(DeviceRegisterRequest request, String token) {
        String orgRefName = utilService.getCurrentUserOrgRefName(token);

        if (deviceRepo.existsByImeiNumber(request.getImeiNumber())) {
            throw new ResourceAlreadyExistException(request.getImeiNumber() + " is already present in the system.Please contact system admin for further reference.");
        } else {
            Device device = new Device(request.getImeiNumber(), request.getModelType(), orgRefName);
            deviceRepo.save(device);
        }
        return new DeviceResponse("Device registered successfully");
    }

    @Override
    @Transactional
    public void deleteDevice(Long deviceId, String token) {
        String orgRefName = utilService.getCurrentUserOrgRefName(token);
        Device device = deviceRepo.findByDeviceIdAndOrgRefName(deviceId, orgRefName).orElseThrow(() -> new ResourceNotFoundException("device id not found"));

        if (device.getVehicle() != null) {
            Vehicle vehicle = device.getVehicle();
            vehicle.setDevice(null);
            vehicleRepository.save(vehicle);
            redisTransformedMessageRepository.deleteVehicleInfoByVehicleNumber(vehicle.getVehicleRegnNumber());
        }

        device.setVehicle(null);
        deviceRepo.saveAndFlush(device);

        redisVehicleInfoRepository.deleteVehicleInfoByImeiNumber(device.getImeiNumber());
        deviceRepo.delete(device);
    }

//    @Override
//    public void deleteDevice(Long deviceId, String token) {
//        String orgRefName = utilService.getCurrentUserOrgRefName(token);
//        deviceRepo.findByDeviceIdAndOrgRefName(deviceId, orgRefName)
//                .ifPresentOrElse(device -> removeDeviceFromDB(device, orgRefName), () -> new ResourceNotFoundException("device id not found"));
//    }
//
//    public void removeDeviceFromDB(Device device, String orgRefName) {
//        try {
//            vehicleDeviceMapperRepository.findByDeviceIdAndOrgRefName(device.getDeviceId(), orgRefName)
//                    .ifPresent(record -> vehicleDeviceMapperRepository.delete(record));
//            deviceRepo.delete(device);
//        } catch (Exception exception) {
//            log.error("Error while removing the device.-> {} ", exception.getLocalizedMessage());
//        }
//
//    }

    @Override
    public List<Device> getDeviceList(String token) {
        String orgRefName = utilService.getCurrentUserOrgRefName(token);
        return deviceRepo.findByOrgRefName(orgRefName).orElse(Collections.emptyList());
    }

    @Override
    public Device getDeviceByIdAndToken(Long deviceId, String token) {
        String orgRefName = utilService.getCurrentUserOrgRefName(token);
        return deviceRepo.findByDeviceIdAndOrgRefName(deviceId, orgRefName)
                .orElseThrow(() -> new ResourceNotFoundException("Requested device not found " + deviceId));
    }

    @Override
    public DeviceResponse updateDevice(UpdateDeviceRequest request, String token) {

        String orgRefName = utilService.getCurrentUserOrgRefName(token);

        //Check if IMEI number is already registered.
        Optional<Device> dbRecordDevice = deviceRepo.findByDeviceIdAndOrgRefNameNot(request.getDeviceid(), orgRefName);
        if (dbRecordDevice.isPresent()) {
            throw new ResourceAlreadyExistException(request.getImeiNumber() + " is already present in the system.Please contact system admin for next steps.");
        }

        deviceRepo.findByDeviceIdAndOrgRefName(request.getDeviceid(), orgRefName).ifPresentOrElse(device -> updateDeviceInDB(device, request),
                () -> new ResourceNotFoundException("Requested device not found " + request.getDeviceid()));

        return new DeviceResponse("Device updated successfully");
    }

    public void updateDeviceInDB(Device device, UpdateDeviceRequest request) {
        device.setImeiNumber(request.getImeiNumber());
        device.setModel(request.getModelType());
        try {
            deviceRepo.save(device);
        } catch (Exception exception) {
            throw new InternalServerError("Error occurred while updating the device in DB" + exception.getLocalizedMessage());
        }
    }


    @Override
    public List<DeviceListResponse> getDeviceByOrg(String token) {

        String orgRefName = utilService.getCurrentUserOrgRefName(token);
        List<DeviceListResponse> response = deviceRepo.findByOrgRefName(orgRefName).orElse(Collections.emptyList())
                .stream().map(device -> convertToResponse(device)).collect(Collectors.toList());
        Collections.sort(response);

        return response;
    }

    private DeviceListResponse convertToResponse(Device device) {
        return new DeviceListResponse(device.getDeviceId(), device.getImeiNumber(), device.getModel(), device.getCreatedAt(), device.getUpdatedAt());
    }


    @Override
    public Long countDeviceByOrg(String token) {
        String orgRefName = utilService.getCurrentUserOrgRefName(token);
        Integer deviceCount = deviceRepo.findByOrgRefName(orgRefName).map(devices -> devices.size()).orElse(0);
        return Long.valueOf(deviceCount);
    }

    @Override
    public List<Device> getDeviceVehicleInfo(String token) {
        String orgRefName = utilService.getCurrentUserOrgRefName(token);
        List<Device> devices = deviceRepo.findByOrgRefName(orgRefName).orElse(Collections.emptyList());

        return devices;
    }

}
