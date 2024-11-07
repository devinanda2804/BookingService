package com.example.bookingService.feign;

import com.example.bookingService.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name="showtime-service",url="http://localhost:8082/api/admin",configuration = FeignClientConfig.class)
public interface ShowTimeClient {

    @GetMapping("/{showTimeId}/exists")
    public ResponseEntity<Boolean> checkIfShowTimeExists(@PathVariable int showTimeId);
}
