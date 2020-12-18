package com.asiczen.services.vehicle.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateDeviceRequest {

    @NotNull(message = "Device id is a requried field can't be left blank")
    private Long deviceid;

    @NotEmpty(message = "imei number is required/Can't be blank")
    @Pattern(regexp = "^[1-9][0-9]{14}", message = "please input a valid imei number")
    private String imeiNumber;

    @NotEmpty(message = "model type is required/Can't be blank")
    private String modelType;
}
