INSERT INTO booking (user_id, showtime_id, status, movie_id, total_seats, seat_ids, total_amount)
VALUES (90, 200, 'PENDING', 101, 2, '[1, 2]', 240),
       (90, 200, 'PENDING', 101, 2, '[1, 2]', 240),
       (90, 200, 'PENDING', 101, 2, '[1, 2]', 240);



INSERT INTO seats ( seat_name, available, movie_id, showtime_id)
VALUES ( 'A1', true, 101, 200),
       ('A2', true, 101, 200);
