package com.asiczen.services.vehicle.controller;


import com.asiczen.services.vehicle.request.CreateVehicleRequest;
import com.asiczen.services.vehicle.request.UpdateVehicleRequest;
import com.asiczen.services.vehicle.response.ApiResponse;
import com.asiczen.services.vehicle.response.CountResponse;
import com.asiczen.services.vehicle.response.CreateVehicleResponse;
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
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class VehicleController {

    @Autowired
    VehicleServices vehicleService;

    @PostMapping("/vehicle")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateVehicleResponse registerVehicle(@Valid @RequestBody CreateVehicleRequest request, @RequestHeader String authorization) {
        log.trace("Register new vehicle request started.");
        return vehicleService.postNewVehicle(request, authorization);
    }

    @GetMapping("/vehicle")
    public ResponseEntity<?> getVehicleList(@RequestHeader String authorization) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
                "Vehicle List Extracted Successfully", vehicleService.getVehicleList(authorization)));
    }

    @PutMapping("/vehicle")
    public ResponseEntity<?> updateVehicle(@Valid @RequestBody UpdateVehicleRequest updateVehicleRequest, @RequestHeader String authorization) {
        return new ResponseEntity<>(vehicleService.updateVehicle(updateVehicleRequest, authorization), HttpStatus.OK);
    }

    @GetMapping("/vehicle/count")
    @ResponseStatus(HttpStatus.OK)
    public CountResponse countVehicle(@RequestHeader String authorization) {
        return new CountResponse(vehicleService.countVehicleByOrg(authorization));
    }

    @DeleteMapping("/vehicle/{vehicleId}")
    public ResponseEntity<?> deleteVehicle(@Valid @PathVariable Long vehicleId, @RequestHeader String authorization) {
        vehicleService.deleteVehicle(vehicleId, authorization);
        return new ResponseEntity<>("Vehicle deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/vehicleinfo")
    public ResponseEntity<?> getAllVehicles(@RequestHeader String authorization) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
                "Vehicle information Extracted Successfully", vehicleService.generateVehicleInfo(authorization)));
    }

    @GetMapping("/getvehicle")
    public ResponseEntity<?> getVehiclebyDevice(@RequestParam String imei) {
        return new ResponseEntity<>(vehicleService.getVehicleNumberByDevice(imei), HttpStatus.OK);
    }

    @GetMapping("/vehicledevicedata")
    public ResponseEntity<?> getVehicleDeviceInfo(@RequestHeader String authorization) {
        return new ResponseEntity<>(vehicleService.vehicleDeviceData(authorization), HttpStatus.OK);
    }
}
