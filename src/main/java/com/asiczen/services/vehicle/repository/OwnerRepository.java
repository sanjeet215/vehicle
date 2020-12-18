package com.asiczen.services.vehicle.repository;


import com.asiczen.services.vehicle.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

	boolean existsByOwnerContactAndOrgRefName(String contactNumber,String orgRefName);

	public Optional<Owner> findByOwnerContactAndOrgRefName(String contactNumber, String orgRefName);
}