package com.example.bookingService.service;

import com.example.bookingService.dto.BookingResponseDto;
import com.example.bookingService.dto.SeatDto;
import com.example.bookingService.dto.ShowtimeAvailabilityDTO;
import com.example.bookingService.feign.ShowTimeClient;
import com.example.bookingService.model.Booking;
import com.example.bookingService.model.Seats;
import com.example.bookingService.repository.BookingRepository;
import com.example.bookingService.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;



    @Autowired
    private ShowTimeClient showTimeClient;

    @Autowired
    private SeatRepository seatRepository;

    public boolean areSeatsAvailable(List<Integer> seatIds, Integer showtimeId, Integer movieId) {
        List<Seats> seats = seatRepository.findAllByIdAndShowtimeIdAndMovieIdAndAvailableTrue(seatIds, showtimeId, movieId);
        return seats.size() == seatIds.size();
    }

    public BookingResponseDto createBooking(Integer userId, Integer showtimeId, Integer movieId, Integer totalSeats, List<Integer> seatIds) {

        Boolean showtimeExists = showTimeClient.checkIfShowTimeExists(showtimeId).getBody();
        if (showtimeExists == null || !showtimeExists) {
            throw new RuntimeException("Showtime does not exist");
        }

        if (!areSeatsAvailable(seatIds, showtimeId, movieId)) {
            throw new RuntimeException("Some of the seats are already booked.");
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setShowtimeId(showtimeId);
        booking.setMovieId(movieId);
        booking.setStatus("PENDING");
        booking.setTotalSeats(totalSeats);
        booking.setSeatIds(seatIds.toString());

        int price = 120;
        int totalAmount = price * totalSeats;
        booking.setTotalAmount(totalAmount);

        booking = bookingRepository.save(booking);


        Booking finalBooking = booking;
        seatIds.forEach(seatId -> {
            Seats seat = seatRepository.findByIdAndShowtimeIdAndMovieId(seatId, showtimeId, movieId)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            seat.setAvailable(false);
            seat.setBooking(finalBooking);
            seatRepository.save(seat);
        });

        return toBookingResponseDto(booking);
    }

    private BookingResponseDto toBookingResponseDto(Booking booking){
        BookingResponseDto dto=new BookingResponseDto();
        dto.setUserId(booking.getUserId());
        dto.setMovieId(booking.getMovieId());
        dto.setShowtimeId(booking.getShowtimeId());
        dto.setStatus(booking.getStatus());
        dto.setTotalSeats(booking.getTotalSeats());
        dto.setSeatIds(booking.getSeatIds());
        dto.setTotalAmount(booking.getTotalAmount());

        return dto;
    }


    public List<ShowtimeAvailabilityDTO> getAvailableShowtimes(Integer movieId) {

        List<Seats> availableSeats = seatRepository.findByMovieIdAndAvailable(movieId, true);


        Map<Integer, List<Seats>> seatsByShowtime = new HashMap<>();
        for (Seats seat : availableSeats) {
            int showtimeId = seat.getShowtimeId();

            if (!seatsByShowtime.containsKey(showtimeId)) {
                seatsByShowtime.put(showtimeId, new ArrayList<>());
            }
            seatsByShowtime.get(showtimeId).add(seat);
        }


        List<ShowtimeAvailabilityDTO> availableShowtimes = new ArrayList<>();
        for (Integer showtimeId : seatsByShowtime.keySet()) {
            List<Seats> seats = seatsByShowtime.get(showtimeId);

            List<String> seatNames = new ArrayList<>();
            for (Seats seat : seats) {
                seatNames.add(seat.getSeatName());
            }

            availableShowtimes.add(new ShowtimeAvailabilityDTO(showtimeId, seats.size(), seatNames));
        }

        return availableShowtimes;
    }



    public List<SeatDto> getAvailableSeats(Integer movieId, Integer showtimeId) {

        List<Seats> availableSeats = seatRepository.findByMovieIdAndShowtimeIdAndAvailable(movieId, showtimeId, true);

        List<SeatDto> seatDtos = new ArrayList<>();
        for (Seats seat : availableSeats) {
            SeatDto seatDto = new SeatDto(seat.getId(), seat.getSeatName(), seat.getAvailable());
            seatDtos.add(seatDto);
        }

        return seatDtos;
    }



    public List<Booking> getBookings(Integer userId) {
        return bookingRepository.findByUserId(userId);
    }


    public void updateStatus(Integer bookingId, String status) {
        bookingRepository.updateBookingStatus(bookingId, status);
    }

    public void cancelBookingAndMakeSeatsAvailable(Integer bookingId){

        if(!bookingRepository.existsById(bookingId)){
            throw new RuntimeException("Booking ID not found");
        }
        else{

            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));


            List<Seats> bookedSeats = seatRepository.findByBookingIdAndAvailable(bookingId ,false);

            bookedSeats.forEach(seat -> seat.setAvailable(true));

            seatRepository.saveAll(bookedSeats);


        }
    }




}