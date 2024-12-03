package com.example.bookingService.service;

import com.example.bookingService.dto.BookingResponseDto;
import com.example.bookingService.dto.ShowtimeAvailabilityDTO;
import com.example.bookingService.feign.ShowTimeClient;
import com.example.bookingService.model.Booking;
import com.example.bookingService.model.Seats;
import com.example.bookingService.repository.BookingRepository;
import com.example.bookingService.repository.SeatRepository;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ShowTimeClient showTimeClient;

    @Mock
    private SeatRepository seatRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void areSeatsAvailableTest() {
        Integer showtimeId = 101;
        Integer movieId = 1;
        List<Integer> seatIds = Arrays.asList(1, 2);
        List<Seats> availableSeats = Arrays.asList(
                new Seats(1, "A1", showtimeId, movieId, true, null),
                new Seats(2, "A2", showtimeId, movieId, true, null)
        );

        when(seatRepository.findAllByIdAndShowtimeIdAndMovieIdAndAvailableTrue(seatIds, showtimeId, movieId))
                .thenReturn(availableSeats);

        boolean result = bookingService.areSeatsAvailable(seatIds, showtimeId, movieId);
        assertTrue(result, "Seats should be available");
    }


    @Test
    public void createBookingTest() {
        Integer userId = 102;
        Integer showtimeId = 101;
        Integer movieId = 200;
        Integer totalSeats = 2;
        List<Integer> seatIds = Arrays.asList(8, 9);

        when(showTimeClient.checkIfShowTimeExists(showtimeId)).thenReturn(ResponseEntity.ok(true));

        List<Seats> availableSeats = Arrays.asList(
                new Seats(8, "A1", showtimeId, movieId, true, null),
                new Seats(9, "A2", showtimeId, movieId, true, null)
        );
        when(seatRepository.findAllByIdAndShowtimeIdAndMovieIdAndAvailableTrue(seatIds, showtimeId, movieId))
                .thenReturn(availableSeats);


        Booking savedBooking = new Booking();
        savedBooking.setId(1);
        savedBooking.setUserId(userId);
        savedBooking.setShowtimeId(showtimeId);
        savedBooking.setMovieId(movieId);
        savedBooking.setStatus("PENDING");
        savedBooking.setTotalSeats(totalSeats);
        savedBooking.setSeatIds(seatIds.toString());
        savedBooking.setTotalAmount(120 * totalSeats);

        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        when(seatRepository.findByIdAndShowtimeIdAndMovieId(8, showtimeId, movieId))
                .thenReturn(Optional.of(availableSeats.get(0)));
        when(seatRepository.findByIdAndShowtimeIdAndMovieId(9, showtimeId, movieId))
                .thenReturn(Optional.of(availableSeats.get(1)));

        BookingResponseDto response = bookingService.createBooking(userId, showtimeId, movieId, totalSeats, seatIds);

        assertNotNull(response, "Booking response should not be null");
        assertEquals("PENDING", response.getStatus(), "Booking status should be PENDING");
        assertEquals(120 * totalSeats, response.getTotalAmount(), "Total amount should match calculated value");
        verify(showTimeClient).checkIfShowTimeExists(showtimeId);
        verify(seatRepository).findAllByIdAndShowtimeIdAndMovieIdAndAvailableTrue(seatIds, showtimeId, movieId);
        verify(bookingRepository).save(any(Booking.class));
        verify(seatRepository, times(seatIds.size())).save(any(Seats.class));
    }


    @Test
    public void getAvailableShowtimesTest(){

        Integer movieId=101;
        Integer showtimeId=110;
        List<Seats> availableSeats = Arrays.asList(
                new Seats(8, "A1", showtimeId, movieId, true, null),
                new Seats(9, "A2", showtimeId, movieId, true, null)
        );

        when(seatRepository.findByMovieIdAndAvailable(movieId, true)).thenReturn(availableSeats);


        List<ShowtimeAvailabilityDTO> availableShowtimes = bookingService.getAvailableShowtimes(movieId);
        assertEquals(1, availableShowtimes.size());


        ShowtimeAvailabilityDTO showtime1 = availableShowtimes.get(0);
        assertEquals(110, showtime1.getShowtimeId());
        assertEquals(2, showtime1.getAvailableSeatsCount());

    }


    @Test
    public void getBookingsTest(){

        Integer userId=101;
        List<Booking> bookings=Arrays.asList(new Booking(1,userId,200,"CONFIRMED",101,2,"3,4",240));

        when(bookingRepository.findByUserId(userId)).thenReturn(bookings);
        List<Booking> expected=bookingService.getBookings(userId);

        assertEquals(bookings.size(),expected.size(),"Size must be same");
        assertEquals(bookings,expected,"Booking list should match");

    }

    @Test
    public void updateStatusTest() {
        Integer bookingId = 1;
        String newStatus = "CONFIRMED";

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStatus("PENDING");
        booking.setUserId(101);
        booking.setMovieId(200);
        booking.setTotalSeats(2);
        booking.setSeatIds("3,4");
        booking.setTotalAmount(240);

        doNothing().when(bookingRepository).updateBookingStatus(bookingId, newStatus);

        bookingService.updateStatus(bookingId, newStatus);

        verify(bookingRepository, times(1)).updateBookingStatus(bookingId, newStatus);
    }


    @Test
    public void cancelBookingAndMakeSeatsAvailableTest() {
        Integer bookingId = 110;

        Booking booking = new Booking(bookingId, 101, 200, "CONFIRMED", 101, 2, "3,4", 240);
        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        List<Seats> bookedSeats = Arrays.asList(
                new Seats(3, "A1", booking.getShowtimeId(), booking.getMovieId(), false, booking),
                new Seats(4, "A2", booking.getShowtimeId(), booking.getMovieId(), false, booking)
        );
        when(seatRepository.findByBookingIdAndAvailable(bookingId, false)).thenReturn(bookedSeats);


        bookingService.cancelBookingAndMakeSeatsAvailable(bookingId);

        for (Seats seat : bookedSeats) {
            assertTrue(seat.getAvailable());
        }

        verify(seatRepository, times(1)).saveAll(bookedSeats);
    }

}





