package com.asiczen.services.vehicle.repository;

import com.asiczen.services.vehicle.model.ErrorTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorTableRepository extends JpaRepository<ErrorTable, Long> {

}
