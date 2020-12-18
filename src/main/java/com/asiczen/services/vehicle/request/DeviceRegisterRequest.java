package com.asiczen.services.vehicle.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class DeviceRegisterRequest {

	@NotEmpty(message = "imei number is required/Can't be blank")
	@Pattern(regexp = "^[1-9][0-9]{14}", message = "please input a valid imei number")
	private String imeiNumber;

	@NotEmpty(message = "model type is required/Can't be blank")
	private String modelType;
}
