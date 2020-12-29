package com.asiczen.services.vehicle.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleDeviceResponse {

    private Long vehicleId;
    private String vehicleNumber;
    private String vehicleType;
    private Long deviceId;
    private String imeiNumber;
}
