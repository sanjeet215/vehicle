package com.asiczen.services.vehicle.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

public interface CSVFileServices {

    boolean isCSVFormattedFile(MultipartFile file);

    void uploadDeviceData(MultipartFile file, String token);

    void uploadVehicleAndOwnerData(MultipartFile file, String token);

    ByteArrayInputStream downloadDeviceData(String token);


}
