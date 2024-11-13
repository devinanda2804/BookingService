package com.example.bookingService.controller;

import com.example.bookingService.dto.BookingResponseDto;
import com.example.bookingService.dto.SeatDto;
import com.example.bookingService.dto.ShowtimeAvailabilityDTO;
import com.example.bookingService.model.Booking;
import com.example.bookingService.dto.BookingRequest;
import com.example.bookingService.service.BookingService;
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

    public ResponseEntity<BookingResponseDto> bookSeats(@RequestBody BookingRequest bookingRequest) {
            BookingResponseDto bookingResponseDto = bookingService.createBooking(
                    bookingRequest.getUserId(),
                    bookingRequest.getShowtimeId(),
                    bookingRequest.getMovieId(),
                    bookingRequest.getTotalSeats(),
                    bookingRequest.getSeatIds());
            return ResponseEntity.status(HttpStatus.CREATED).body(bookingResponseDto);
    }



    @GetMapping("/available-seats/{movieId}/{showtimeId}")
    public ResponseEntity<List<SeatDto>> getAvailableSeats(@PathVariable Integer movieId, @PathVariable Integer showtimeId) {
        List<SeatDto> availableSeats = bookingService.getAvailableSeats(movieId, showtimeId);
        return ResponseEntity.ok(availableSeats);
    }

    @GetMapping("/available-showtimes/{movieId}")
    public List<ShowtimeAvailabilityDTO> getAvailableShowtimes(@PathVariable Integer movieId) {
        return bookingService.getAvailableShowtimes(movieId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Booking>> getBookings(@PathVariable Integer userId) {
        List<Booking> bookingList = bookingService.getBookings(userId);
        return ResponseEntity.ok(bookingList);
    }




    @PutMapping("/{bookingId}/status")
    public void updateStatus(@PathVariable Integer bookingId, @RequestParam("status") String status) {
        bookingService.updateStatus(bookingId, status);
    }



    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable Integer bookingId) {
        bookingService.cancelBookingAndMakeSeatsAvailable(bookingId);
        return ResponseEntity.ok("Your booking has been canceled, and the seat(s) are now available.");
    }

}
