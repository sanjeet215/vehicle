package com.asiczen.services.vehicle.serviceimpl;


import com.asiczen.services.vehicle.exception.AccessisDeniedException;
import com.asiczen.services.vehicle.exception.InternalServerError;
import com.asiczen.services.vehicle.exception.ResourceAlreadyExistException;
import com.asiczen.services.vehicle.exception.ResourceNotFoundException;
import com.asiczen.services.vehicle.model.Driver;
import com.asiczen.services.vehicle.model.Vehicle;
import com.asiczen.services.vehicle.repository.DriverRepository;
import com.asiczen.services.vehicle.request.CreateDriverRequest;
import com.asiczen.services.vehicle.request.UpdateDriverRequest;
import com.asiczen.services.vehicle.response.CreateDriverResponse;
import com.asiczen.services.vehicle.response.DriverListResponse;
import com.asiczen.services.vehicle.response.UpdateDriverResponse;
import com.asiczen.services.vehicle.services.DriverServices;
import com.asiczen.services.vehicle.services.VehicleServices;
import com.asiczen.services.vehicle.utility.UtilityServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DriverServicesImpl implements DriverServices {

    @Autowired
    DriverRepository driverRepo;

    @Autowired
    UtilityServices utilService;

    @Autowired
    VehicleServices vehicleServices;

    @Override
    public CreateDriverResponse registerDriver(CreateDriverRequest request, String token) {

        String orgRefName = getOrganizationFromToken(token);

        /* Driver contact number and DL number should be unique */
        log.trace("Checking if driver contact number already exists!");

        if (driverRepo.existsByContactNumberAndOrgRefName(request.getContactNumber(), orgRefName)) {
            throw new ResourceAlreadyExistException("Contact number is already registered.");
        }

        if (driverRepo.existsByDrivingLicenceAndOrgRefName(request.getDrivingLicence(), orgRefName)) {
            throw new ResourceAlreadyExistException("Driver is already registered. Please check DL number");
        }

        driverRepo.save(new Driver(request.getDriverName(), request.getContactNumber(), request.getWhatsappnumber(), request.getDrivingLicence(), orgRefName));

        return new CreateDriverResponse("Driver registered successfully.");
    }

    @Override
    public UpdateDriverResponse updateDriver(UpdateDriverRequest request, String token) {

        String orgRefName = getOrganizationFromToken(token);

        Driver driver = driverRepo.findByDriverIdAndOrgRefName(request.getDriverId(), orgRefName)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with supplied id " + request.getDriverId()));

        try {
            setDriver(driver, request);
            driverRepo.save(driver);
        } catch (Exception exception) {
            throw new InternalServerError("Error occurred while saving the object." + exception.getLocalizedMessage());
        }

        return new UpdateDriverResponse("Diver details updated successfully");
    }

    public Driver setDriver(Driver driver, UpdateDriverRequest driverUpdateRequest) {
        driver.setFullName(driverUpdateRequest.getDriverName());
        driver.setDrivingLicence(driverUpdateRequest.getDrivingLicence());
        driver.setWhatsAppNumber(driverUpdateRequest.getWhatsappnumber());
        driver.setContactNumber(driverUpdateRequest.getContactNumber());
        driver.setUpdatedAt(getCurrentTimeStamp());
        return driver;
    }

    private Date getCurrentTimeStamp() {
        Date date = new Date();
        return date;
    }

    @Override
    public boolean existsDriverContactNumberAndOrgRefName(String contactNumber, String orgRefName) {
        return driverRepo.existsByContactNumberAndOrgRefName(contactNumber, orgRefName);
    }

    @Override
    public boolean existsDlNumberAndOrgRefName(String dlNumber, String orgRefName) {
        return driverRepo.existsByDrivingLicenceAndOrgRefName(dlNumber, orgRefName);
    }

    @Override
    public List<DriverListResponse> getDriverListbyOrg(String token) {

        String orgRefName = getOrganizationFromToken(token);

        List<Driver> driversList = driverRepo.findByOrgRefName(orgRefName)
                .orElseThrow(() -> new ResourceNotFoundException("No drivers registered for organization yet"));

        List<DriverListResponse> response = process(driversList);
        Collections.sort(response);

        return response;
    }

    public List<DriverListResponse> process(List<Driver> driverList) {
        return driverList.stream().map(driver -> new DriverListResponse(driver.getDriverId(), driver.getFullName(),
                driver.getContactNumber(), driver.getWhatsAppNumber(), driver.getDrivingLicence(),
                driver.getCreatedAt(), driver.getUpdatedAt())).collect(Collectors.toList());
    }

    @Override
    public Long countDriverByOrg(String token) {

        String orgRefName = getOrganizationFromToken(token);
        Optional<List<Driver>> driverList = driverRepo.findByOrgRefName(orgRefName);
        return (driverList.isPresent()) ? driverList.get().size() : 0L;
    }

    @Override
    public Driver getDriverById(Long driverId, String token) {
        String orgRefName = getOrganizationFromToken(token);
        return driverRepo.findByDriverIdAndOrgRefName(driverId, orgRefName).orElseThrow();
    }

    @Override
    public void deleteDriverById(Long driverId, String token) {
        String orgRefName = getOrganizationFromToken(token);
        driverRepo.findByDriverIdAndOrgRefName(driverId, orgRefName).ifPresentOrElse(driver -> deleteDriver(driver), () -> new ResourceNotFoundException("invalid request."));
    }

    @Override
    public Driver getDriverByVehicleId(long vehicleId, String token) {

        String orgRefName = getOrganizationFromToken(token);
        Vehicle vehicle = vehicleServices.findVehicleByVehicleId(vehicleId, orgRefName);
        return driverRepo.findByOrgRefNameAndVehicles(orgRefName, vehicle).stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Mo driver registered for the vehicle."));
    }


    private void deleteDriver(Driver driver) {

        try {
            log.info("Driver deletion started.");
            driver.getVehicles().clear();
            driverRepo.delete(driver);
            log.info("Driver deletion finished.");
        } catch (Exception exception) {
            log.error("Error while deleting the driver.");
        }
    }

    private String getOrganizationFromToken(String token) {
        String orgRefName = utilService.getCurrentUserOrgRefName(token);
        return Optional.ofNullable(orgRefName).orElseThrow(() -> new AccessisDeniedException("Access is denied."));
    }


}
