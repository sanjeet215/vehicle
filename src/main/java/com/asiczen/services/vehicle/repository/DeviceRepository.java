package com.asiczen.services.vehicle.repository;

import com.asiczen.services.vehicle.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

	boolean existsByImeiNumber(String imeiNumber);

	boolean existsByImeiNumberAndOrgRefName(String imeiNumber,String orgRefName);

	Optional<Device> findByImeiNumberAndOrgRefName(String imeiNumber, String orgRefName);

	Optional<List<Device>> findByOrgRefName(String orgRefName);

	Optional<Device> findByDeviceIdAndOrgRefName(Long deviceId, String orgRefName);

	Optional<Device> findByDeviceIdAndOrgRefNameNot(Long deviceId,String orgRefName);
}
