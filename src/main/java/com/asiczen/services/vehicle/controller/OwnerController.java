package com.asiczen.services.vehicle.controller;

import com.asiczen.services.vehicle.model.Owner;
import com.asiczen.services.vehicle.services.OwnerServices;
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
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class OwnerController {

    @Autowired
    OwnerServices ownerServices;

    @GetMapping("/ownerbyvehicle/{vehicleId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Owner> getOwnerDetailsFromVehicle(@Valid @PathVariable Long vehicleId, @RequestHeader String authorization) {
        return new ResponseEntity<Owner>(ownerServices.findOwnerByVehicleId(vehicleId, authorization), HttpStatus.OK);
    }
}
