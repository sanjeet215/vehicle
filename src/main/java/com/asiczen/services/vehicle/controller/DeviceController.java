package com.asiczen.services.vehicle.controller;

import com.asiczen.services.vehicle.request.DeviceRegisterRequest;
import com.asiczen.services.vehicle.request.UpdateDeviceRequest;
import com.asiczen.services.vehicle.response.ApiResponse;
import com.asiczen.services.vehicle.response.CountResponse;
import com.asiczen.services.vehicle.response.DeviceResponse;
import com.asiczen.services.vehicle.services.DeviceServices;
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
public class DeviceController {

    @Autowired
    DeviceServices deviceService;

    @PostMapping("/device")
    @ResponseStatus(HttpStatus.OK)
    public DeviceResponse registerDevice(@Valid @RequestBody DeviceRegisterRequest request,@RequestHeader String authorization) {
        log.trace("Register device method is invoked. --> {} ", request.toString());
        return deviceService.registerDevice(request, authorization);
    }

    @GetMapping("/device")
    public ResponseEntity<?> getDeviceList(@RequestHeader String authorization) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
                "Device List Extracted Successfully", deviceService.getDeviceByOrg(authorization)));
    }

    @DeleteMapping("/device/{deviceId}")
    public ResponseEntity<?> deleteDevice(@Valid @PathVariable Long deviceId, @RequestHeader String authorization) {
        deviceService.deleteDevice(deviceId, authorization);
        return new ResponseEntity<>("Device deleted successfully", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/device")
    @ResponseStatus(HttpStatus.OK)
    public DeviceResponse updateDevice(@Valid @RequestBody UpdateDeviceRequest request, @RequestHeader String authorization) {
        return deviceService.updateDevice(request, authorization);
    }

    @GetMapping("/device/count")
    @ResponseStatus(HttpStatus.OK)
    public CountResponse getVehicleCount(@RequestHeader String authorization) {
        return new CountResponse(deviceService.countDeviceByOrg(authorization));
    }

    @GetMapping("/deviceinfo")
    public ResponseEntity<?> getDeviceAndVehicleInfo(@RequestHeader String authorization) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
                "Device List Extracted Successfully", deviceService.getDeviceVehicleInfo(authorization)));
    }
}
