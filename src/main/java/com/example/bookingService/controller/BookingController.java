package com.example.bookingService.controller;

import com.example.bookingService.dto.SeatDto;
import com.example.bookingService.model.Booking;
import com.example.bookingService.dto.BookingRequest;
import com.example.bookingService.model.BookingSeats;
import com.example.bookingService.model.Seats;
import com.example.bookingService.service.BookingService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/book")

    public ResponseEntity<Booking> bookSeats(@RequestBody BookingRequest bookingRequest) {
            Booking booking = bookingService.createBooking(
                    bookingRequest.getUserId(),
                    bookingRequest.getShowtimeId(),
                    bookingRequest.getMovieId(),
                    bookingRequest.getTotalSeats(),
                    bookingRequest.getSeatIds());
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }


    @GetMapping("/available-seats/{movieId}/{showtimeId}")
    public ResponseEntity<List<SeatDto>> getAvailableSeats(@PathVariable Integer movieId, @PathVariable Integer showtimeId) {
        List<SeatDto> availableSeats = bookingService.getAvailableSeats(movieId, showtimeId);
        return ResponseEntity.ok(availableSeats);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<Booking>> getBookings(@PathVariable Integer userId) {
        List<Booking> bookingList = bookingService.getBookings(userId);
        return ResponseEntity.ok(bookingList);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> deleteBookings(@PathVariable Integer bookingId){
        bookingService.deleteBookingSeatsByBookingId(bookingId);
        return ResponseEntity.ok("Your booking has been deleted");
    }

}
