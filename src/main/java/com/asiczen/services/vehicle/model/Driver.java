package com.asiczen.services.vehicle.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "driver")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Driver extends AuditModel implements Serializable {

    private static final long serialVersionUID = -3427327908834988872L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;
    private String fullName;
    private String contactNumber;
    private String whatsAppNumber;
    private String drivingLicence;

    //driver vehicle foregien key
	@OneToMany(targetEntity = Vehicle.class,cascade = CascadeType.ALL)
	@JoinColumn(name = "dv_fk",referencedColumnName = "driverId")
	private List<Vehicle> vehicles;

	@NotBlank
    private String orgRefName;

    public Driver(String fullName, String contactNumber, String whatsAppNumber, String drivingLicence, String orgRefName) {
        super();
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.whatsAppNumber = whatsAppNumber;
        this.drivingLicence = drivingLicence;
        this.orgRefName = orgRefName;
    }

}
