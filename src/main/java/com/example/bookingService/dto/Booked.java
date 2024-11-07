package com.example.bookingService.dto;

import lombok.Data;

import java.util.List;

@Data
public class Booked {
    private Integer Id;
    private Integer userId;
    private Integer showtimeId;
    private Integer movieId;
    private String status;
    private Integer totalSeats;
    private List<Integer> seatIds;
    private Integer totalAmount;

}
