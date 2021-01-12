package com.asiczen.services.vehicle.repository;

import com.asiczen.services.vehicle.dto.TransformedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RedisTransformedMessageRepository {

    private static final String KEY = "LASTVINFO";

    private HashOperations<String, String, TransformedMessage> hashOperations;

    private RedisTemplate<String, TransformedMessage> redisTemplate;

    public RedisTransformedMessageRepository(RedisTemplate<String, TransformedMessage> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public boolean deleteVehicleInfoByVehicleNumber(String vehicleNumber) {
        try {
            hashOperations.delete(KEY, vehicleNumber);
        } catch (Exception exception) {
            log.error("Error while removing the vehicle data from redis....");
            log.error(">>>>>>  {} ", exception.getLocalizedMessage());
            return false;
        }

        return true;
    }

}
