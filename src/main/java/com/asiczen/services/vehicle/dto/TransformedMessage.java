package com.asiczen.services.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class TransformedMessage {

    // Original Fields
    private String imei;
    private int gps;
    private double lat;
    private double lng;
    private boolean isKeyOn;
    private int heading;
    private Date timestamp;
    private int unplugged;
    private int fuel;
    private int speed;

    // Derieved Fields
    private String vehicleNumber;
    private String vehicleType;
    private String driverName;
    private String driverContact;
    private String orgRefName;

    private double calculatedSpeed;
    private double averageSpeed;
    private double calulatedDistance;
    private double calculatedDailyDistance;
    private double topSpeed;

    // Seconds
    private int idleKeyOnTime; // Vehicle Key is on but vehicle is not moving
    private int idleKeyOffTime; // Key is off and Vehicle is not moving
    private int vehicleMovingTime; // key is on and vehicle is moving

    // Tank
    private double currentFuel;

    // Flags for Different status
    boolean idleEngineOn;
    boolean idleEngineOff;
    boolean vehicleMovingFlag;
    boolean alertFlag;
    boolean currentFlag = true;
    boolean geoViolation;
    boolean speedViolation;

    // Total Number of Messages
    int messageCounter;
}
