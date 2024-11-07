package com.example.bookingService.service;

import com.example.bookingService.dto.BookingRequest;
import com.example.bookingService.dto.SeatDto;
import com.example.bookingService.feign.ShowTimeClient;
import com.example.bookingService.model.Booking;
import com.example.bookingService.model.BookingSeats;
import com.example.bookingService.model.Seats;
import com.example.bookingService.repository.BookingRepository;
import com.example.bookingService.repository.BookingSeatsRepository;
import com.example.bookingService.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingSeatsRepository bookingSeatsRepository;

    @Autowired
    private ShowTimeClient showTimeClient;

    @Autowired
    private SeatRepository seatRepository;

    public boolean areSeatsAvailable(List<Integer> seatIds, Integer showtimeId, Integer movieId) {

        List<BookingSeats> existingBookings = bookingSeatsRepository.findBySeatIdsAndShowtimeIdAndMovieId(seatIds, showtimeId, movieId);
        return existingBookings.isEmpty();
    }

    public Booking createBooking(Integer userId, Integer showtimeId, Integer movieId, Integer totalSeats, List<Integer> seatIds) {

        Boolean showtimeExists=showTimeClient.checkIfShowTimeExists(showtimeId).getBody();

        if(showtimeExists==null || !showtimeExists){
            throw new RuntimeException("Showtime does not exist");
        }

        if (!areSeatsAvailable(seatIds, showtimeId, movieId)) {
            throw new RuntimeException("Some of the seats are already booked.");
        }

        Booking booking = new Booking();

        booking.setMovieId(movieId);
        booking.setShowtimeId(showtimeId);
        booking.setStatus("PENDING");
        booking.setTotalSeats(totalSeats);
        booking.setUserId(userId);
        booking.setSeatIds(seatIds.toString());

        int price=120;
        int totalAmount=price* totalSeats;
        booking.setTotalAmount(totalAmount);
        bookingRepository.save(booking);


        booking = bookingRepository.save(booking);


        Booking finalBooking = booking;
        List<BookingSeats> bookingSeatsList = seatIds.stream()
                .map(seatId -> {
                    Seats seat = seatRepository.findById(seatId)
                            .orElseThrow(() -> new RuntimeException("Seat not found"));
                    BookingSeats bookingSeat = new BookingSeats();
                    bookingSeat.setBooking(finalBooking);
                    bookingSeat.setSeat(seat);
                    bookingSeat.setShowtimeId(showtimeId);
                    bookingSeat.setMovieId(movieId);
                    return bookingSeat;
                })
                .collect(Collectors.toList());

        bookingSeatsRepository.saveAll(bookingSeatsList);

        return booking;
    }


    public List<SeatDto> getAvailableSeats(Integer movieId, Integer showtimeId) {

        List<Seats> allSeats = seatRepository.findAll();


        List<Integer> bookedSeatIds = bookingSeatsRepository.findBookedSeatIdsByShowtimeIdAndMovieId(showtimeId, movieId);

        List<SeatDto> availableSeats = allSeats.stream()
                .filter(seat -> !bookedSeatIds.contains(seat.getId()))
                .map(seat -> new SeatDto(seat.getId(), seat.getSeatName()))
                .collect(Collectors.toList());

        return availableSeats;
    }


    public List<Booking> getBookings(Integer userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteBookingSeatsByBookingId(Integer bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new RuntimeException("Booking ID not found.");
        } else {
            bookingRepository.deleteById(bookingId);
        }
    }


}