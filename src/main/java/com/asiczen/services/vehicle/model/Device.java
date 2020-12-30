package com.asiczen.services.vehicle.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "device")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device extends AuditModel implements Serializable {

    private static final long serialVersionUID = -8997780194128158796L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;

    @Column(nullable = false, unique = true)
    private String imeiNumber;

    private String model;

    @NotBlank
    private String orgRefName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "device")
    @JsonManagedReference
    private Vehicle vehicle;

    public Device(String imeiNumber, String model, String orgRefName) {
        super();
        this.imeiNumber = imeiNumber;
        this.model = model;
        this.orgRefName = orgRefName;
    }

    @Override
    public String toString() {
        return imeiNumber + "," + model;
    }
}