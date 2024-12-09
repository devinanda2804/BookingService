package com.example.bookingService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "seat_name", nullable = false, unique = true)
    private String seatName;

    @Column(name="showtime_id")
    private Integer showtimeId;

    @Column(name="movie_id")
    private Integer movieId;

    @Column(name="available")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name="booking_id",referencedColumnName = "id")
    private Booking booking;


}
