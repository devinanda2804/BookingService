package com.example.bookingService.repository;

import com.example.bookingService.model.Booking;
import com.example.bookingService.model.BookingSeats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    void deleteById(Integer bookingId);
    boolean existsById(Integer bookingId);

    List<Booking> findByUserId(Integer userId);
}
