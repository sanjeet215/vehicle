package com.asiczen.services.vehicle.controller;

import com.asiczen.services.vehicle.request.CreateDriverRequest;
import com.asiczen.services.vehicle.request.UpdateDriverRequest;
import com.asiczen.services.vehicle.response.CountResponse;
import com.asiczen.services.vehicle.response.CreateDriverResponse;
import com.asiczen.services.vehicle.response.DriverListResponse;
import com.asiczen.services.vehicle.response.UpdateDriverResponse;
import com.asiczen.services.vehicle.services.DriverServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/fleet")
@Slf4j
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class DriverController {

    @Autowired
    DriverServices driversService;

    @PostMapping("/driver")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateDriverResponse addNewDriver(@Valid @RequestBody CreateDriverRequest request, @RequestHeader String authorization) {

        log.trace("Received new driver registration request ..");
        log.trace("parameters are as follows");
        log.trace(request.toString());
        return driversService.registerDriver(request, authorization);
    }

    @GetMapping("/driver")
    @ResponseStatus(HttpStatus.OK)
    public List<DriverListResponse> getDriverbyId(@RequestHeader String authorization) {
        return driversService.getDriverListbyOrg(authorization);
    }

    @PutMapping("/driver")
    @ResponseStatus(HttpStatus.OK)
    public UpdateDriverResponse updateDriver(@Valid @RequestBody UpdateDriverRequest request, @RequestHeader String authorization) {
        return driversService.updateDriver(request, authorization);
    }

    @DeleteMapping("/driver/{driverid}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteDriver(@Valid @PathVariable Long driverid, @RequestHeader String authorization) {
        driversService.deleteDriverById(driverid, authorization);
        return new ResponseEntity<>("driver deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/driver/count")
    @ResponseStatus(HttpStatus.OK)
    public CountResponse countDriverbyOrg(@RequestHeader String authorization) {
        return new CountResponse(driversService.countDriverByOrg(authorization));
    }
}
