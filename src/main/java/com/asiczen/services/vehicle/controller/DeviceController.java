package com.asiczen.services.vehicle.controller;

import com.asiczen.services.vehicle.exception.FileUploadException;
import com.asiczen.services.vehicle.request.DeviceRegisterRequest;
import com.asiczen.services.vehicle.request.UpdateDeviceRequest;
import com.asiczen.services.vehicle.response.ApiResponse;
import com.asiczen.services.vehicle.response.CountResponse;
import com.asiczen.services.vehicle.response.DeviceResponse;
import com.asiczen.services.vehicle.response.FileUploadResponse;
import com.asiczen.services.vehicle.services.CSVFileServices;
import com.asiczen.services.vehicle.services.DeviceServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fleet")
@Slf4j
public class DeviceController {

    @Autowired
    DeviceServices deviceService;

    @Autowired
    CSVFileServices csvFileServices;

    @PostMapping("/device")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DeviceResponse registerDevice(@Valid @RequestBody DeviceRegisterRequest request,@RequestHeader String authorization) {
        log.trace("Register device method is invoked. --> {} ", request.toString());
        return deviceService.registerDevice(request, authorization);
    }

    @GetMapping("/device")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getDeviceList(@RequestHeader String authorization) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
                "Device List Extracted Successfully", deviceService.getDeviceByOrg(authorization)));
    }

    @DeleteMapping("/device/{deviceId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteDevice(@Valid @PathVariable Long deviceId, @RequestHeader String authorization) {
        deviceService.deleteDevice(deviceId, authorization);
        return new ResponseEntity<>("Device deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/device")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DeviceResponse updateDevice(@Valid @RequestBody UpdateDeviceRequest request, @RequestHeader String authorization) {
        return deviceService.updateDevice(request, authorization);
    }

    @GetMapping("/device/count")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CountResponse getVehicleCount(@RequestHeader String authorization) {
        return new CountResponse(deviceService.countDeviceByOrg(authorization));
    }

    @GetMapping("/deviceinfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getDeviceAndVehicleInfo(@RequestHeader String authorization) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(),
                "Device List Extracted Successfully", deviceService.getDeviceVehicleInfo(authorization)));
    }


    @PostMapping("/device/upload")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public FileUploadResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader String authorization) {

        if (csvFileServices.isCSVFormattedFile(file)) {
            throw new FileUploadException("Invalid file format. please check the format");
        }
        csvFileServices.uploadDeviceData(file,authorization);

        return new FileUploadResponse("File uploaded successfully.");
    }


    @GetMapping("/device/download")
    public ResponseEntity<Resource> downloadFile(@RequestHeader String authorization) {

        String filename = "devices.csv";

        InputStreamResource file = new InputStreamResource(csvFileServices.downloadDeviceData(authorization));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
//    @GetMapping("/device/error")
//    public ResponseEntity<ApiResponse> getErrorListForOrganizationFileUpload(@RequestHeader String authorization) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ApiResponse(HttpStatus.OK.value(), "Errors extracted.", errorTableServices.getAllErrorDetails()));
//    }
//
//    @GetMapping("/device/errorflag")
//    public ResponseEntity<ApiResponse> getErrorListForOrganizationFileUpload(@Valid @RequestParam boolean status, @RequestHeader String authorization) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ApiResponse(HttpStatus.OK.value(), "Errors extracted.", errorTableServices.getAllErrorDetailsWithFlag(status)));
//    }
}
