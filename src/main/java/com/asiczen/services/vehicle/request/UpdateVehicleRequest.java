package com.asiczen.services.vehicle.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class UpdateVehicleRequest extends CreateVehicleRequest {

	@NotNull(message = "vehicle id is a requried field can't be left blank")
	private Long vehicleid;
}
