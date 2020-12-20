package com.asiczen.services.vehicle.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleDeviceResponse {

    private Long vehicleId;
    private String vehicleNumber;
    private String vehicleType;
    private Long deviceId;
    private String imeiNumber;
}
