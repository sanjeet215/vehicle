package com.asiczen.services.vehicle.serviceimpl;

import com.asiczen.services.vehicle.exception.ResourceAlreadyExistException;
import com.asiczen.services.vehicle.exception.ResourceNotFoundException;
import com.asiczen.services.vehicle.model.Device;
import com.asiczen.services.vehicle.model.Owner;
import com.asiczen.services.vehicle.model.Vehicle;
import com.asiczen.services.vehicle.repository.DeviceRepository;
import com.asiczen.services.vehicle.repository.DriverRepository;
import com.asiczen.services.vehicle.repository.VehicleRepository;
import com.asiczen.services.vehicle.request.CreateVehicleRequest;
import com.asiczen.services.vehicle.request.UpdateVehicleRequest;
import com.asiczen.services.vehicle.response.*;

import com.asiczen.services.vehicle.repository.OwnerRepository;
import com.asiczen.services.vehicle.services.VehicleServices;
import com.asiczen.services.vehicle.utility.UtilityServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class VehicleServicesImpl implements VehicleServices {

    @Autowired
    VehicleRepository vehicleRepo;

    @Autowired
    OwnerRepository ownerRepo;

    @Autowired
    UtilityServices utilService;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Transactional
    @Override
    public CreateVehicleResponse postNewVehicle(CreateVehicleRequest request, String token) {

        String orgRefName = utilService.getCurrentUserOrgRefName(token);
        Vehicle vehicle = new Vehicle();

        log.trace("Creating new vehicle...");
        log.trace("Method received parameters --> {} ", request.toString());
        log.trace("Check if vehicle number is already registered");

        if (!vehicleRepo.existsByVehicleRegnNumber(request.getVehicleRegNumber())) {

            vehicle.setVehicleRegnNumber(request.getVehicleRegNumber());
            vehicle.setVehicleType(request.getVehcleType());
            vehicle.setOrgRefName(orgRefName);

        } else {
            throw new ResourceAlreadyExistException("Vehicle number is already registered. " + request.getVehicleRegNumber());
        }

        Owner vehicleOwner = new Owner();

        if (ownerRepo.existsByOwnerContactAndOrgRefName(request.getOwnerContact(), orgRefName)) {
            vehicleOwner = ownerRepo.findByOwnerContactAndOrgRefName(request.getOwnerContact(), orgRefName).get();
            vehicleOwner.getVehicles().add(vehicle);

        } else {
            vehicleOwner.setOwnerContact(request.getOwnerContact());
            vehicleOwner.setOwnerName(request.getOwnerName());
            vehicleOwner.setOrgRefName(orgRefName);
            List<Vehicle> vehicleList = new ArrayList<>();
            vehicleList.add(vehicle);
            vehicleOwner.setVehicles(vehicleList);
        }


        try {
            vehicleRepo.save(vehicle);
            ownerRepo.save(vehicleOwner);

        } catch (Exception exception) {
            log.error("Error while persisting data in database.");
            log.error(exception.getLocalizedMessage());
        }


        return new CreateVehicleResponse("Vehicle saved successfully");
    }

    @Override
    @Transactional
    public UpdateVehicleResponse updateVehicle(UpdateVehicleRequest request, String token) {
        String orgRefName = utilService.getCurrentUserOrgRefName(token);

        Optional<Vehicle> vehicle = vehicleRepo.findByVehicleIdAndOrgRefName(request.getVehicleid(), orgRefName);

        if (vehicle.isPresent()) {
            vehicle.get().setVehicleRegnNumber(request.getVehicleRegNumber());
            vehicle.get().setVehicleType(request.getVehcleType());
            vehicle.get().setOrgRefName(orgRefName);
            vehicle.get().setUpdatedAt(new Date());

            vehicleRepo.save(vehicle.get());
        } else {
            log.error("invalid vehicle update request");
            throw new ResourceNotFoundException("Vehicle id not found for update");
        }

        Owner vehicleOwner = ownerRepo.findByOwnerContactAndOrgRefName(request.getOwnerContact(), orgRefName)
                .orElseGet(() -> new Owner(request.getOwnerName(), request.getOwnerContact(), orgRefName));

        vehicleOwner.setUpdatedAt(new Date());
        ownerRepo.save(vehicleOwner);

        return new UpdateVehicleResponse("Vehicle information updated successfully.");
    }

    @Override
    public long countVehicleByOrg(String token) {

        String orgRefName = utilService.getCurrentUserOrgRefName(token);

        Optional<List<Vehicle>> vehicles = vehicleRepo.findByOrgRefName(orgRefName);
        return (vehicles.isPresent()) ? vehicles.get().size() : 0L;

    }

    @Override
    public Object generateVehicleInfo(String token) {
        String orgRefName = utilService.getCurrentUserOrgRefName(token);
        return driverRepository.findByOrgRefName(orgRefName);
    }

    @Override
    public VehicleDetailsFromLinkedImei getVehicleNumberByDevice(String imei) {
        Device device = deviceRepository.findByImeiNumber(imei).orElseThrow(() -> new ResourceNotFoundException("Invalid imei number"));
        VehicleDetailsFromLinkedImei response = new VehicleDetailsFromLinkedImei();
        response.setOrgRefName(device.getOrgRefName());
        response.setVehicleNumber(device.getVehicle().getVehicleRegnNumber());
        BeanUtils.copyProperties(device.getVehicle(), response);
        return response;
    }

    @Override
    public List<VehicleDeviceResponse> vehicleDeviceData(String token) {
        String orgRefName = utilService.getCurrentUserOrgRefName(token);
        return vehicleRepo.findByOrgRefName(orgRefName).stream()
                .flatMap(recordList -> recordList.stream())
                .flatMap(vehicle -> generateVehicleDeviceResponse(vehicle))
                .collect(Collectors.toList());

    }

    private Stream<VehicleDeviceResponse> generateVehicleDeviceResponse(Vehicle vehicle) {

        VehicleDeviceResponse response = new VehicleDeviceResponse();
        try {
            if (vehicle.getDevice() != null) {
                response.setDeviceId(vehicle.getDevice().getDeviceId());
                response.setImeiNumber(vehicle.getDevice().getImeiNumber());
            }
            response.setVehicleNumber(vehicle.getVehicleRegnNumber());
            response.setVehicleId(vehicle.getVehicleId());
            response.setVehicleType(vehicle.getVehicleType());
            return Stream.of(response);
        } catch (Exception exception) {
            return Stream.empty();
        }
    }

    @Override
    public List<VehicleListResponse> getVehicleList(String token) {
        String orgRefName = utilService.getCurrentUserOrgRefName(token);

        Optional<List<Vehicle>> optVehicles = vehicleRepo.findByOrgRefName(orgRefName);

        if (optVehicles.isPresent()) {

            List<VehicleListResponse> response = optVehicles.get().stream().map(this::returnResponse).collect(Collectors.toList());
            Collections.sort(response);
            return response;

        } else {
            throw new ResourceNotFoundException("No vehicles registered yet..");
        }

    }

    private VehicleListResponse returnResponse(Vehicle record) {
        VehicleListResponse response = new VehicleListResponse();
        BeanUtils.copyProperties(record, response);
        return response;
    }

    @Transactional
    @Override
    public void deleteVehicle(Long vehicleId, String token) {
        log.info("Deleting Vehicle with Vehicle id : {}", vehicleId);

        String orgRefName = utilService.getCurrentUserOrgRefName(token);

        Vehicle vehicle = vehicleRepo.findByVehicleIdAndOrgRefName(vehicleId, orgRefName)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid vehicle id to delete."));

        removeVehicle(vehicle, orgRefName);
    }

    public void removeVehicle(Vehicle vehicle, String orgRefName) {

        try {
            log.trace("Deleting vehicle .................");
            log.trace("Vehicle ID {}", vehicle.getVehicleId());
            vehicle.setDevice(null);
            vehicleRepo.delete(vehicle);
            log.trace("Delete finished ..................");
        } catch (Exception exception) {
            log.error("There is an exception");
            log.error(exception.getLocalizedMessage());
        }
    }


}
