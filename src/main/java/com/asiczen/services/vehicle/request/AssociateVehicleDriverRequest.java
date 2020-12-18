package com.asiczen.services.vehicle.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssociateVehicleDriverRequest {

    @NotNull(message = "Vehicle id is a requried field can't be left blank")
    private Long vehicleId;

    @NotNull(message = "Driver id is a requried field can't be left blank")
    private Long driverId;
}
