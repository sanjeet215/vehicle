package com.asiczen.services.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleInfo implements Serializable {

	private static final long serialVersionUID = -2670607325919116429L;

	String imei;
	String vehicleNumber;
	String vehicleType;
	String driverName;
	String driverNumber;
	String orgRefName;

}