package com.example.bookingService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "booking_seats", uniqueConstraints = @UniqueConstraint(columnNames = {"booking_id", "seat_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonBackReference
    private Booking booking;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seats seat;

    @Column(name = "showtime_id", nullable = false)
    private Integer showtimeId;

    @Column(name = "movie_id", nullable = false)
    private Integer movieId;


}
