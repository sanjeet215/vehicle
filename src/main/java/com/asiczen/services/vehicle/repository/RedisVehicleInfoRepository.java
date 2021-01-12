package com.asiczen.services.vehicle.repository;


import com.asiczen.services.vehicle.dto.VehicleInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

// Imei number is mapped as the key -- Dependency is there on message processor IOT. So consider that service in case any changes in this service.

@Repository
@Slf4j
public class RedisVehicleInfoRepository {

    private static final String KEY = "VINFO";

    private HashOperations<String, String, VehicleInfo> hashOperations;

    private RedisTemplate<String, VehicleInfo> redisTemplate;

    public RedisVehicleInfoRepository(RedisTemplate<String, VehicleInfo> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public void save(VehicleInfo vehicleInfo) {
        hashOperations.put(KEY, vehicleInfo.getImei(), vehicleInfo);
    }

    public boolean deleteVehicleInfoByImeiNumber(String imeiNumber) {
        try {
            hashOperations.delete(KEY, imeiNumber);
        } catch (Exception exception) {
            log.error("Error while removing the device from redis....");
            log.error(">>>>>>  {} ", exception.getLocalizedMessage());
            return false;
        }

        return true;
    }


}
