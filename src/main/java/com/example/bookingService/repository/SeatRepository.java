package com.example.bookingService.repository;

import com.example.bookingService.model.Seats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seats, Integer> {

}
