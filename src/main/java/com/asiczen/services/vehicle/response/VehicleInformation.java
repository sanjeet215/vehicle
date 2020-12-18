package com.asiczen.services.vehicle.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude (JsonInclude.Include.NON_EMPTY)
public class VehicleInformation {

    private int status;
    private String message;
    private Object data;

}
