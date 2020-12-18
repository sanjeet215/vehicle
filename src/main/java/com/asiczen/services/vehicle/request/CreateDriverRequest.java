package com.asiczen.services.vehicle.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class CreateDriverRequest {

	@NotEmpty(message = "Driver Name is required/Can't be blank")
	@Size(min = 1, max = 100, message = "Driver Name should be between 1 to 100 characters")
	private String driverName;

	@NotEmpty(message = "contact Number is required/Can't be blank")
	private String contactNumber;

	@NotEmpty(message = "contact Number is required/Can't be blank")
	private String whatsappnumber;

	@NotEmpty(message = "DL is required/Can't be blank")
	private String drivingLicence;
}
