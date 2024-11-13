package com.example.bookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ShowtimeAvailabilityDTO {
    private int showtimeId;
    private int availableSeatsCount;
    private List<String> seats;
}
