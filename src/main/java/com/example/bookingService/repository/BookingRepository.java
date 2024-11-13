package com.example.bookingService.repository;

import com.example.bookingService.model.Booking;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    void deleteById(Integer bookingId);
    boolean existsById(Integer bookingId);

    List<Booking> findByUserId(Integer userId);

    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.status = :status WHERE b.id = :bookingId")
    void updateBookingStatus(@Param("bookingId") Integer bookingId, @Param("status") String status);

}
