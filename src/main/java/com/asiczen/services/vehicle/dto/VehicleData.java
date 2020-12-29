package com.asiczen.services.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VehicleData {

    @NotEmpty(message = "Registration number is required/Can't be blank")
    @Size(min = 3, max = 50, message = "Registration number should be between 3 to 20 characters")
    String vehicleRegNumber;

    @NotEmpty(message = "Vehicle type is required/Can't be blank")
    String vehcleType;

    @NotEmpty(message = "Owner Name is required/Can't be blank")
    String ownerName;

    @NotEmpty(message = "Owner contact number is required/Can't be blank")
    String ownerContact;

}
