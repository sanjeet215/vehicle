package com.asiczen.services.vehicle.services;


import com.asiczen.services.vehicle.model.Device;
import com.asiczen.services.vehicle.request.DeviceRegisterRequest;
import com.asiczen.services.vehicle.request.UpdateDeviceRequest;
import com.asiczen.services.vehicle.response.DeviceListResponse;
import com.asiczen.services.vehicle.response.DeviceResponse;

import java.util.List;

public interface DeviceServices {

	public DeviceResponse registerDevice(DeviceRegisterRequest request, String token);

	public void deleteDevice(Long deviceId, String token);

	public List<Device> getDeviceList(String token);

	public Device getDeviceByIdAndToken(Long deviceId,String token);

	// public List<DeviceInfoResponse> getDeviceInfo(String token);

	public DeviceResponse updateDevice(UpdateDeviceRequest request, String token);

	public List<DeviceListResponse> getDeviceByOrg(String token);

	public Long countDeviceByOrg(String token);
}
