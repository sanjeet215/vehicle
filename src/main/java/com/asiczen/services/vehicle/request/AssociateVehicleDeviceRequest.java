package com.asiczen.services.vehicle.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class AssociateVehicleDeviceRequest {

    @NotNull(message = "Vechile id is a required field can't be left blank")
    Long vehicleid;

    @NotNull(message = "Device id is a required field can't be left blank")
    Long deviceid;
}
