package com.asiczen.services.vehicle.repository;


import com.asiczen.services.vehicle.model.Owner;
import com.asiczen.services.vehicle.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    boolean existsByOwnerContactAndOrgRefName(String contactNumber, String orgRefName);

    Optional<Owner> findByOwnerContactAndOrgRefName(String contactNumber, String orgRefName);

    Optional<List<Owner>> findByOrgRefName(String orgRefName);

    Optional<Owner> findByOrgRefNameAndVehicles(String ordRefName, Vehicle vehicle);
}