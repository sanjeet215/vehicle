package com.asiczen.services.vehicle.utility;


import com.asiczen.services.vehicle.response.CurrentUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UtilityServices {

	@Autowired
	RestTemplate restTemplate;

	@Value("${BASEURL}")
	private String BASEURL;

	public String getCurrentUserOrgRefName(String token) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", token);
		log.trace("token --> {}", token);
		HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

		try {

			ResponseEntity<CurrentUserResponse> response = restTemplate.exchange(BASEURL + "currentuser",
					HttpMethod.GET, requestEntity, CurrentUserResponse.class);
			log.trace("Received Response {} ", response.toString());
			return response.getBody().getOrgRefName();
		} catch (Exception ep) {
			log.error("No organization registered against user id");
			log.error(ep.getLocalizedMessage());
			return null;
		}

	}


	//public String get
}
