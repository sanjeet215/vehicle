package com.asiczen.services.vehicle.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleDetailsFromLinkedImei {
    String vehicleNumber;
    String driverName;
    String driverNumber;
    String orgRefName;
    String vehicleType;
}
