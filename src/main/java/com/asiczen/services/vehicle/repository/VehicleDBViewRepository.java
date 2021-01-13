package com.asiczen.services.vehicle.repository;

import com.asiczen.services.vehicle.model.VehicleDBView;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface VehicleDBViewRepository extends JpaRepository<VehicleDBView, Long> {

    Optional<VehicleDBView> findByVehicleNumber(String vehicleNumber);
}
