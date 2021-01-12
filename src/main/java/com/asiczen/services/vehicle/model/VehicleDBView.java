package com.asiczen.services.vehicle.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "vehicledbview")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleDBView implements Serializable {

    private static final long serialVersionUID = -2670607325919116429L;

    @Id
    private long vehicleId;
    private String imei;
    private String vehicleNumber;
    private String vehicleType;
    private String driverName;
    private String driverNumber;
    private String orgRefName;

}