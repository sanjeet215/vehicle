package com.asiczen.services.vehicle.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DriverListResponse implements Comparable<DriverListResponse> {

	private Long driverId;
	private String driverName;
	private String contactNumber;
	private String whatsappnumber;
	private String drivingLicence;
	private Date createdTime;
	private Date updatedTime;

	@Override
	public int compareTo(DriverListResponse otherDriver) {
		return updatedTime.compareTo(otherDriver.getUpdatedTime()) * (-1);
	}

}
