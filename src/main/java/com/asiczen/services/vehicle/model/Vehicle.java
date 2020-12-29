package com.asiczen.services.vehicle.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "vehicle")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Vehicle extends AuditModel implements Serializable {

    private static final long serialVersionUID = 2151154239365240757L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    private String vehicleRegnNumber;

    private String vehicleType;

    @NotBlank
    private String orgRefName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dev_fk")
    @JsonBackReference
    private Device device;

    public Vehicle(String vehicleRegnNumber, String vehicleType, String orgRefName) {
        this.vehicleRegnNumber = vehicleRegnNumber;
        this.vehicleType = vehicleType;
        this.orgRefName = orgRefName;
    }


}
