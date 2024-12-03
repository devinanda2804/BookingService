package com.example.bookingService.dto;

import lombok.Data;

@Data
public class BookingResponseDto {

    private Integer id;
    private Integer userId;
    private Integer showtimeId;

    private String status;

    private Integer movieId;

    private Integer totalSeats;

    private String seatIds;

    private Integer totalAmount;

}

