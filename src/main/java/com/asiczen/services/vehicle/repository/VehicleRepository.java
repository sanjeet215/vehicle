package com.asiczen.services.vehicle.repository;

import com.asiczen.services.vehicle.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

import static org.hibernate.jpa.QueryHints.HINT_PASS_DISTINCT_THROUGH;

@Repository
@Transactional(readOnly = true)
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	boolean existsByVehicleRegnNumber(String regNumber);

	Optional<Vehicle> findByVehicleRegnNumber(String vehicleRegnNumber);

	@QueryHints(value = @QueryHint(name = HINT_PASS_DISTINCT_THROUGH, value = "false"))
	Optional<List<Vehicle>> findByOrgRefName(String orgRefName);

	Optional<Vehicle> findByVehicleIdAndOrgRefName(Long vehicleId, String orgRefName);

	List<Vehicle> findByOrgRefNameAndVehicleIdIn(String orgRefName, List<Long> vehicleids);
}
