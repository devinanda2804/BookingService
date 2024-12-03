package com.example.bookingService.controller;

import com.example.bookingService.dto.BookingRequest;
import com.example.bookingService.dto.BookingResponseDto;
import com.example.bookingService.feign.ShowTimeClient;
import com.example.bookingService.model.Seats;
import com.example.bookingService.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/test.sql"})
public class BookingControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;


    @MockBean
    private ShowTimeClient showTimeClient;

    @MockBean
    private SeatRepository seatRepository;

    @BeforeEach
    public void setup() {

        testRestTemplate = testRestTemplate.withBasicAuth("admin", "admin");



    }

    @Test
    public void testBookSeats_SeatsNotAvailable() {

        Mockito.when(showTimeClient.checkIfShowTimeExists(200))
                .thenReturn(ResponseEntity.ok(true));
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setUserId(90);
        bookingRequest.setShowtimeId(200);
        bookingRequest.setMovieId(101);
        bookingRequest.setTotalSeats(2);
        bookingRequest.setSeatIds(Arrays.asList(1, 2));


        ResponseEntity<Map> response = testRestTemplate.postForEntity(
                "/api/bookings/book", bookingRequest, Map.class);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

    }

    @Test
    public void testCreateBooking_Success() {

        Mockito.when(showTimeClient.checkIfShowTimeExists(200))
                .thenReturn(ResponseEntity.ok(true));

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setUserId(90);
        bookingRequest.setShowtimeId(200);
        bookingRequest.setMovieId(101);
        bookingRequest.setTotalSeats(2);
        bookingRequest.setSeatIds(Arrays.asList(1, 2));



        ResponseEntity<BookingResponseDto> response = testRestTemplate.postForEntity(
                "/api/bookings/book", bookingRequest, BookingResponseDto.class);


        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Unexpected HTTP status!");
        assertNotNull(response.getBody(), "Response body is null!");
        BookingResponseDto responseBody = response.getBody();
        assertEquals(90, responseBody.getUserId());
        assertEquals(200, responseBody.getShowtimeId());
        assertEquals(101, responseBody.getMovieId());
        assertEquals(2, responseBody.getTotalSeats());
        assertEquals("PENDING", responseBody.getStatus());
        assertEquals("[1, 2]", responseBody.getSeatIds());
        assertEquals(240, responseBody.getTotalAmount());
    }



    @Test
    public void testGetAvailableSeats() {

        int movieId = 101;
        int showtimeId = 200;

        ResponseEntity<List> response = testRestTemplate.getForEntity(
                "/api/bookings/available-seats/" + movieId + "/" + showtimeId,
                List.class
        );


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println("Available Seats: " + response.getBody());
    }

    @Test
    public void testGetAvailableShowtimes() {
        int movieId = 101;

        ResponseEntity<List> response = testRestTemplate.getForEntity(
                "/api/bookings/available-showtimes/" + movieId,
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        System.out.println("Available Showtimes: " + response.getBody());
    }

    @Test
    public void testGetBookings() {

        int userId = 90;


        ResponseEntity<List> response = testRestTemplate.getForEntity(
                "/api/bookings/" + userId,
                List.class
        );


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        System.out.println("Bookings: " + response.getBody());
    }
}



