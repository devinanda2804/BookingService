package com.example.bookingService.repository;

import com.example.bookingService.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookingRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;
    private Booking booking;

    @BeforeEach
    public void setUp() {
        booking = new Booking();
        booking.setUserId(100);
        booking.setTotalAmount(120);
        booking.setMovieId(100);
        booking.setShowtimeId(200);
        booking.setTotalSeats(2);
        booking.setSeatIds("6,7");
        booking.setStatus("PENDING");
        bookingRepository.save(booking);

        System.out.println("Booking initial status: " + booking.getStatus());
    }

    @Test
    public void deleteByIdTest() {
        bookingRepository.deleteById(booking.getId());
        assertFalse(bookingRepository.existsById(booking.getId()), "Booking should be deleted");
    }


    @Test
    public void existByIdTest() {
        boolean exists = bookingRepository.existsById(booking.getId());
        assertTrue(exists, "Booking should exist in the repository");
    }

    @Test
    void testFindByUserId() {
        List<Booking> bookings = bookingRepository.findByUserId(booking.getUserId());
        assertEquals(booking.getUserId(), bookings.get(0).getUserId());
    }


    @Test

    public void testUpdateBookingStatus() {

        Booking booking = new Booking();
        booking.setUserId(100);
        booking.setTotalAmount(120);
        booking.setMovieId(100);
        booking.setShowtimeId(200);
        booking.setTotalSeats(2);
        booking.setSeatIds("6,7");
        booking.setStatus("confirmed");
        booking = bookingRepository.save(booking);


        bookingRepository.updateBookingStatus(booking.getId(), "confirmed");
        bookingRepository.flush();
        Booking updatedBooking = bookingRepository.findById(booking.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        assertEquals("confirmed", updatedBooking.getStatus(), "Booking status should be updated to 'confirmed'");
    }

}
