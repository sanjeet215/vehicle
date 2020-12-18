package com.asiczen.services.vehicle.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@ToString
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

    public Device(String imeiNumber, String model, String orgRefName) {
        super();
        this.imeiNumber = imeiNumber;
        this.model = model;
        this.orgRefName = orgRefName;
    }
}