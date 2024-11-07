
package com.example.bookingService.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {
    private Integer userId;
    private Integer showtimeId;
    private Integer movieId;
    private Integer totalSeats;
    private List<Integer> seatIds;
}
