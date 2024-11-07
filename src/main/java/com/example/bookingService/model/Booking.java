package com.example.bookingService.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "showtime_id", nullable = false)
    private Integer showtimeId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "movie_id", nullable = false)
    private Integer movieId;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "seat_ids")
    private String seatIds;

    @Column(name="total_Amount")
    private Integer totalAmount;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<BookingSeats> bookingSeats = new ArrayList<>();
}
