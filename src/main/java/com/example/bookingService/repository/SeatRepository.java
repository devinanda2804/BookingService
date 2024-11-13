package com.example.bookingService.repository;

import com.example.bookingService.model.Seats;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seats, Integer> {


    @Query("SELECT s FROM Seats s WHERE s.id IN :seatIds AND s.showtimeId = :showtimeId AND s.movieId = :movieId AND s.available = true")
    List<Seats> findAllByIdAndShowtimeIdAndMovieIdAndAvailableTrue(
            @Param("seatIds") List<Integer> seatIds,
            @Param("showtimeId") Integer showtimeId,
            @Param("movieId") Integer movieId
    );

    @Query("SELECT s FROM Seats s WHERE s.id = :seatId AND s.showtimeId = :showtimeId AND s.movieId = :movieId")
    Optional<Seats> findByIdAndShowtimeIdAndMovieId(
            @Param("seatId") Integer seatId,
            @Param("showtimeId") Integer showtimeId,
            @Param("movieId") Integer movieId
    );


    List<Seats> findByMovieIdAndAvailable(Integer movieId, Boolean available);



    List<Seats> findByMovieIdAndShowtimeIdAndAvailable(Integer movieId, Integer showtimeId, Boolean available);

    List<Seats> findByBookingIdAndAvailable(Integer bookingId, boolean available);



}
