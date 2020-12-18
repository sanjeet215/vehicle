package com.asiczen.services.vehicle.response;


import com.asiczen.services.vehicle.model.Owner;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleListResponse implements Comparable<VehicleListResponse>, Serializable {

	private static final long serialVersionUID = -4061566887611657319L;

	private Long vehicleId;
	private String vehicleRegnNumber;
	private String vehicleType;
	private Owner owner;

	private Date createdAt;
	private Date updatedAt;

	@Override
	public int compareTo(VehicleListResponse otherVehicle) {
		return updatedAt.compareTo(otherVehicle.getUpdatedAt()) * (-1);
	}
}
