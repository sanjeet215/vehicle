package com.asiczen.services.vehicle.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceInfo {

    private Long deviceId;
    private String imeiNumber;
    private String model;
    private Vehicle vehicle;

    public DeviceInfo(Long deviceId, String imeiNumber, String model) {
        this.deviceId = deviceId;
        this.imeiNumber = imeiNumber;
        this.model = model;
    }
}
