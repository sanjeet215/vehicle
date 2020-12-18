package com.asiczen.services.vehicle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "vehicleowner")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Owner extends AuditModel implements Serializable {

	private static final long serialVersionUID = -600076266507833877L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vehicleownerId;

	private String ownerName;

	private String ownerContact;

	@OneToMany(targetEntity = Vehicle.class,cascade = CascadeType.ALL)
	@JoinColumn(name = "ov_fk",referencedColumnName = "vehicleownerId")
	private List<Vehicle> vehicles;

	@NotBlank
	private String orgRefName;

	public Owner(String ownerName,String ownerContact,String orgRefName){
		this.ownerName = ownerName;
		this.ownerContact = ownerContact;
		this.orgRefName = orgRefName;
	}
}
