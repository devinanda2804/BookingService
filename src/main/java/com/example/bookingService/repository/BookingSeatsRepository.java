package com.example.bookingService.repository;

import com.example.bookingService.model.BookingSeats;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookingSeatsRepository extends JpaRepository<BookingSeats, Integer> {

    List<BookingSeats> findBySeatIdInAndShowtimeIdAndMovieId(List<Integer> seatIds, Integer showtimeId, Integer movieId);


    @Query("SELECT bse.seat.id FROM BookingSeats bse WHERE bse.booking.showtimeId = :showtimeId AND bse.booking.movieId = :movieId")
    List<Integer> findBookedSeatIdsByShowtimeIdAndMovieId(@Param("showtimeId") Integer showtimeId, @Param("movieId") Integer movieId);




}