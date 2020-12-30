package com.asiczen.services.vehicle.serviceimpl;

import com.asiczen.services.vehicle.dto.CSVDeviceData;
import com.asiczen.services.vehicle.dto.DeviceData;
import com.asiczen.services.vehicle.dto.OwnerDetails;
import com.asiczen.services.vehicle.dto.VehicleData;
import com.asiczen.services.vehicle.exception.InternalServerError;
import com.asiczen.services.vehicle.exception.ResourceAlreadyExistException;
import com.asiczen.services.vehicle.exception.ResourceNotFoundException;
import com.asiczen.services.vehicle.model.Device;
import com.asiczen.services.vehicle.model.ErrorTable;
import com.asiczen.services.vehicle.model.Owner;
import com.asiczen.services.vehicle.model.Vehicle;
import com.asiczen.services.vehicle.repository.DeviceRepository;
import com.asiczen.services.vehicle.repository.ErrorTableRepository;
import com.asiczen.services.vehicle.repository.OwnerRepository;
import com.asiczen.services.vehicle.services.CSVFileServices;
import com.asiczen.services.vehicle.utility.UtilityServices;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CSVFileServicesImpl implements CSVFileServices {

    private static final String TYPE = "text/csv";

    @Autowired
    DeviceRepository deviceRepo;

    @Autowired
    UtilityServices utilService;

    @Autowired
    ErrorTableRepository errorTableRepository;

    @Autowired
    OwnerRepository ownerRepository;

    @Override
    public boolean isCSVFormattedFile(MultipartFile file) {
        return false;
    }

    @Override
    public void uploadDeviceData(MultipartFile file, String token) {

        String orgRefName = utilService.getCurrentUserOrgRefName(token);

        log.debug("Uploading data for {} ", orgRefName);

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            csvParser.getRecords().parallelStream().map(record -> parseCSVDataForDevice(record)).forEach(processedRecord -> saveDeviceToDatabase(processedRecord, orgRefName));

        } catch (IOException exception) {
            log.error("IO Error while reading the file");
            log.error(exception.getLocalizedMessage());

        } catch (Exception ep) {
            log.error("Error while reading the file");
            log.error(ep.getLocalizedMessage());
        }
    }

    @Override
    public void uploadVehicleAndOwnerData(MultipartFile file, String token) {

        String orgRefName = utilService.getCurrentUserOrgRefName(token);

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<VehicleData> processedRecord = csvParser.getRecords().parallelStream().map(record -> parseCSVDataForVehicleAndOwner(record)).collect(Collectors.toList());
            saveVehicleDataToDatabase(processedRecord, orgRefName);

        } catch (IOException exception) {
            log.error("There is an IO error occured...");
            log.error(exception.getLocalizedMessage());

        } catch (Exception ep) {
            log.error("Error while reading the file....");
            log.error(ep.getLocalizedMessage());
        }

    }

    private void saveVehicleDataToDatabase(List<VehicleData> processedRecord, String orgRefName) {

        // Get distinct vehicle owners by Organization.

        processedRecord.stream().filter(record -> record.getVehicleRegNumber().equalsIgnoreCase("NA")
                || record.getOwnerContact().equalsIgnoreCase("NA") || record.getOwnerName().equalsIgnoreCase("NA"))
                .map(record -> new OwnerDetails(record.getOwnerName(), record.getOwnerContact()))
                .distinct()
                .filter(owner -> !ownerRepository.existsByOwnerContactAndOrgRefName(owner.getOwnerContact(), orgRefName))
                .forEach(owner -> ownerRepository.save(new Owner(owner.getOwnerName(), owner.getOwnerContact(), orgRefName)));

        //Owner Contact Number , List<VehicleData> map
        Map<String, List<VehicleData>> vehicleOwnerMap = processedRecord.stream().collect(Collectors.groupingBy(VehicleData::getOwnerContact));
        vehicleOwnerMap.forEach((key, value) -> generateVehicleAndOwnerObject(key, value, orgRefName));


    }

    private void generateVehicleAndOwnerObject(String contact, List<VehicleData> vehicleList, String orgRefName) {

        try {
            List<Vehicle> vehicles = vehicleList.stream().map(vehicle -> new Vehicle(vehicle.getVehicleRegNumber(), vehicle.getVehcleType(), orgRefName)).collect(Collectors.toList());
            long ownerDuplicateCheck = vehicleList.stream().map(vehicle -> vehicle.getOwnerName()).map(ownerName -> ownerName.toLowerCase()).distinct().count();

            if (ownerDuplicateCheck > 1) {
                throw new ResourceAlreadyExistException("Duplicate owner Names found, Please check.");
            }

            String ownerName = vehicleList.stream().map(vehicle -> vehicle.getOwnerName()).collect(Collectors.toList()).stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("Owner Name is invalid.."));

            Owner owner = new Owner();
            owner.setOwnerContact(contact);
            owner.setVehicles(vehicles);
            owner.setOrgRefName(orgRefName);
            owner.setOwnerName(ownerName);

            ownerRepository.save(owner);
        } catch (ResourceNotFoundException resourceNotFoundException) {
            generateErrorRecord("check the records against the contact number ->" + contact, true, resourceNotFoundException.getLocalizedMessage());
        } catch (ResourceAlreadyExistException resourceAlreadyExistException) {
            generateErrorRecord("Duplicate owner names found again contact number ->" + contact, true, resourceAlreadyExistException.getLocalizedMessage());
        }
    }

    private VehicleData parseCSVDataForVehicleAndOwner(CSVRecord record) {

        VehicleData vehicleData = new VehicleData();

        if (record.get("Vehicle Number") != null) {
            vehicleData.setVehicleRegNumber(record.get("Vehicle Number"));
        } else {
            vehicleData.setVehicleRegNumber("NA");
        }

        if (record.get("Vehicle Type") != null) {
            vehicleData.setVehcleType(record.get("Vehicle Type"));
        } else {
            vehicleData.setVehcleType("NA");
        }

        if (record.get("Owner Name") != null) {
            vehicleData.setOwnerName(record.get("Owner Name"));
        } else {
            vehicleData.setOwnerName("NA");
        }

        if (record.get("Owner Contact") != null) {
            vehicleData.setOwnerContact(record.get("Owner Contact"));
        } else {
            vehicleData.setOwnerContact("NA");
        }

        return vehicleData;
    }

    private void saveDeviceToDatabase(DeviceData processedRecord, String orgRefName) {

        if (processedRecord.getImeiNumber().equalsIgnoreCase("NA")) {
            generateErrorRecord(processedRecord, true, "IMEI number can't be null or NA");
            return;
        }

        try {
            if (!StringUtils.isEmpty(orgRefName) && deviceRepo.existsByImeiNumberAndOrgRefName(processedRecord.getImeiNumber(), orgRefName)) {

                Device device = deviceRepo.findByImeiNumberAndOrgRefName(processedRecord.getImeiNumber(), orgRefName).get();
                device.setImeiNumber(processedRecord.getImeiNumber());
                device.setModel(processedRecord.getModel());
                device.setOrgRefName(orgRefName);
                device.setUpdatedAt(new Date());
                deviceRepo.save(device);

            } else {
                deviceRepo.save(new Device(processedRecord.getImeiNumber(), processedRecord.getModel(), orgRefName));
            }
        } catch (Exception exception) {
            generateErrorRecord(processedRecord.toString(), true, exception.getLocalizedMessage());
        }
    }

    private DeviceData parseCSVDataForDevice(CSVRecord record) {

        DeviceData deviceData = new DeviceData();

        if (record.get("imei_number") != null) {
            deviceData.setImeiNumber(record.get("imei_number"));
        } else {
            deviceData.setImeiNumber("NA");
        }

        if (record.get("model") != null) {
            deviceData.setModel(record.get("model"));
        } else {
            deviceData.setModel("NA");
        }

        return deviceData;

    }

    private void generateErrorRecord(Object data, boolean status, String message) {
        String actualRecord = getActualString(data.toString());
        String errorMessage = getActualString(message);
        ErrorTable error = new ErrorTable(actualRecord, LocalDateTime.now(), status, errorMessage);
        errorTableRepository.save(error);
    }

    private String getActualString(String inputString) {
        String response;
        int length = inputString.length();
        if (length > 250) {
            response = inputString.substring(0, 250);
        } else {
            response = inputString;
        }
        return response;
    }

    @Override
    public ByteArrayInputStream downloadDeviceData(String token) {

        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            csvPrinter.printRecords("imei_number,model");
            deviceRepo.findAll().stream()
                    .map(record -> convertDeviceToData(record))
                    .forEach(record -> writeToStream(csvPrinter, record));
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerError("Some IO error while downloading the file.");
        } catch (Exception e) {
            throw new InternalServerError("Some error while downloading the file.");
        }
    }

    private void writeToStream(CSVPrinter csvPrinter, CSVDeviceData record) {
        try {

            String data = record.getImei_number() + "," + record.getModel();
            csvPrinter.printRecords(data);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error while writing the record into file.");
            log.error(e.getLocalizedMessage());
        }
    }

    private CSVDeviceData convertDeviceToData(Device record) {

        CSVDeviceData response = new CSVDeviceData();
        response.setImei_number(record.getImeiNumber());
        response.setModel(record.getModel());

        return response;
    }

}
