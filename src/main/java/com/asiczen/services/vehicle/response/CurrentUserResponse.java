package com.asiczen.services.vehicle.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserResponse {

	private String uuid;
	private String emailid;
	private String given_name;
	private String orgRefName;

}
