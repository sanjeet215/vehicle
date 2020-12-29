package com.asiczen.services.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeviceData {

    private String imeiNumber;

    private String model;

    private String orgRefName;
}
