/*
package com.example.bookingService.repository;

import com.example.bookingService.model.Booking;
import com.example.bookingService.model.Seats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SeatRepositoryTest {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void setUp() {
        // Insert test data into the H2 database
        seatRepository.saveAll(Arrays.asList(
                new Seats(1, "A1", 101, 201, true, null),
                new Seats(2, "A2", 101, 201, true, null),
                new Seats(3, "B1", 101, 201, false, null),
                new Seats(4, "B2", 102, 202, true, null)
        ));
    }

    @Test
    void testFindAllByIdAndShowtimeIdAndMovieIdAndAvailableTrue() {
        List<Seats> availableSeats = seatRepository.findAllByIdAndShowtimeIdAndMovieIdAndAvailableTrue(
                Arrays.asList(1, 2, 3), 101, 201
        );

        assertEquals(2, availableSeats.size(), "Only 2 seats should be available.");
        assertTrue(availableSeats.stream().allMatch(Seats::getAvailable), "All seats should be available.");
    }

    @Test
    void testFindByIdAndShowtimeIdAndMovieId() {
        Optional<Seats> seat = seatRepository.findByIdAndShowtimeIdAndMovieId(1, 101, 201);

        assertTrue(seat.isPresent(), "Seat should be found.");
        assertEquals("A1", seat.get().getSeatName(), "Seat name should match.");
    }

    @Test
    void testFindByMovieIdAndAvailable() {
        List<Seats> availableSeats = seatRepository.findByMovieIdAndAvailable(201, true);

        assertEquals(3, availableSeats.size(), "There should be 3 available seats for movieId 201.");
    }

    @Test
    void testFindByMovieIdAndShowtimeIdAndAvailable() {
        List<Seats> availableSeats = seatRepository.findByMovieIdAndShowtimeIdAndAvailable(201, 101, true);

        assertEquals(2, availableSeats.size(), "There should be 2 available seats for the given movieId and showtimeId.");
    }

    @Test
    void testFindByBookingIdAndAvailable() {
        // Step 1: Create a Booking instance
        Booking booking = new Booking();
        booking.setId(999); // Simulating a booking ID
        bookingRepository.save(booking); // Persist the booking object (if needed)

        // Step 2: Create a Seats instance associated with the booking
        Seats seat = seatRepository.findById(3).orElseThrow(() -> new RuntimeException("Seat not found"));
        seat.setAvailable(false);
        seat.setBooking(booking); // Associate the seat with the booking
        seatRepository.save(seat); // Save the updated seat

        // Step 3: Query the seats associated with the booking
        List<Seats> bookedSeats = seatRepository.findByBookingIdAndAvailable(999, false);

        // Step 4: Assert the results
        assertEquals(1, bookedSeats.size(), "There should be 1 booked seat for bookingId 999.");
        assertFalse(bookedSeats.get(0).getAvailable(), "The seat should not be available.");
        assertEquals(999, bookedSeats.get(0).getBooking().getId(), "The seat should be associated with booking ID 999.");
    }

}*/
