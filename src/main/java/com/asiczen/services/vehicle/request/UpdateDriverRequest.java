package com.asiczen.services.vehicle.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateDriverRequest extends CreateDriverRequest {

	@NotNull(message = "Driver id is a requried field can't be left blank")
	Long driverId;
}
