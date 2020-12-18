package com.asiczen.services.vehicle.controller;

import com.asiczen.services.vehicle.request.AssociateVehicleDriverRequest;
import com.asiczen.services.vehicle.services.AssociateVehicleDriver;
import com.asiczen.services.vehicle.services.VehicleServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fleet")
@Slf4j
public class AssociationController {

//    @Autowired
//    AssociateVehicleDevice associateVehicleDevice;

    @Autowired
    AssociateVehicleDriver associateVehicleDriver;

//    @Autowired
//    VehicleServices vehicleServices;

    // Associate vehicle and device.
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PostMapping("/devicevehiclemap")
//    public ResponseEntity<?> associateVehicleWithDevice(@Valid @RequestBody AssociateVehicleDeviceRequest request, @RequestHeader String Authorization) {
//        log.trace("Device and Vehicle pairing request has started.");
//        log.trace("Data request --> {} ", request.toString());
//        return new ResponseEntity<>(associateVehicleDevice.associateVehicleDevice(request.getVehicleid(), request.getDeviceid(), Authorization), HttpStatus.CREATED);
//    }

    // Associate vehicle and driver.
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/vehicledrivermap")
    public ResponseEntity<?> associateVehicleWithDriver(@Valid @RequestBody AssociateVehicleDriverRequest request, @RequestHeader String authorization) {
        log.trace("vehicle and driver association process started ..");
        return new ResponseEntity<>(associateVehicleDriver.associateVehicleAndDriver(request.getVehicleId(), request.getDriverId(), authorization), HttpStatus.CREATED);
    }

//    @GetMapping("/getvehicle")
//    public ResponseEntity<?> getVehiclebyDevice(@RequestParam String imei) {
//        return new ResponseEntity<>(vehicleServices.getVehicleNumberByDevice(imei), HttpStatus.OK);
//    }


}
