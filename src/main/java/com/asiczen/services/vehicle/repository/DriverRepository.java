package com.asiczen.services.vehicle.repository;

import java.util.List;
import java.util.Optional;

import com.asiczen.services.vehicle.model.Driver;
import com.asiczen.services.vehicle.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    boolean existsByContactNumberAndOrgRefName(String contactNumber, String orgRefName);

    boolean existsByDrivingLicenceAndOrgRefName(String dlNumber, String orgRefName);

    Optional<Driver> findByDriverIdAndOrgRefName(Long driverId, String orgRefName);

    public Optional<Driver> findByContactNumberAndOrgRefName(String contactNumber, String orgRefName);

    public Optional<List<Driver>> findByOrgRefName(String orgRefName);

    Optional<Driver> findByOrgRefNameAndVehicles(String orgRefName, Vehicle vehicle);
}
