package com.asiczen.services.vehicle.controller;

import com.asiczen.services.vehicle.request.AssociateVehicleDeviceRequest;
import com.asiczen.services.vehicle.request.AssociateVehicleDriverRequest;
import com.asiczen.services.vehicle.services.AssociateVehicleDevice;
import com.asiczen.services.vehicle.services.AssociateVehicleDriver;
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

    @Autowired
    AssociateVehicleDevice associateVehicleDevice;

    @Autowired
    AssociateVehicleDriver associateVehicleDriver;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/vehicledrivermap")
    public ResponseEntity<?> associateVehicleWithDriver(@Valid @RequestBody AssociateVehicleDriverRequest request, @RequestHeader String authorization) {
        log.trace("vehicle and driver association process started ..");
        return new ResponseEntity<>(associateVehicleDriver.associateVehicleAndDriver(request.getVehicleId(), request.getDriverId(), authorization), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/devicevehiclemap")
    public ResponseEntity<?> associateVehicleWithDevice(@Valid @RequestBody AssociateVehicleDeviceRequest request, @RequestHeader String authorization) {
        log.trace("Device and Vehicle pairing request has started.");
        log.trace("Data request --> {} ", request.toString());
        return new ResponseEntity<>(associateVehicleDevice.associateVehicleAndDevice(request.getVehicleid(), request.getDeviceid(), authorization), HttpStatus.CREATED);
    }

}
