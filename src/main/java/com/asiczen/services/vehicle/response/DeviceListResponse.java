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
public class DeviceListResponse implements Comparable<DeviceListResponse> {

	private Long deviceId;
	private String imeiNumber;
	private String model;
	private Date createdTime;
	private Date updatedTime;

	@Override
	public int compareTo(DeviceListResponse otherDevice) {
		return updatedTime.compareTo(otherDevice.getUpdatedTime())*(-1);
	}
}
