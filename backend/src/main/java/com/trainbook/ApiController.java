package com.trainbook;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

record BookingRequest(Long trainId, String passengerName, int seats) {}

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ApiController {
    private final TrainRepository trains;
    private final BookingRepository bookings;
    public ApiController(TrainRepository t, BookingRepository b) { trains = t; bookings = b; }

    @GetMapping("/trains")
    public List<Train> search(@RequestParam(defaultValue = "") String from, @RequestParam(defaultValue = "") String to) {
        return trains.findByFromStationContainingIgnoreCaseAndToStationContainingIgnoreCase(from, to);
    }

    @PostMapping("/bookings")
    @Transactional
    public Booking book(@RequestBody BookingRequest r) {
        Train t = trains.findById(r.trainId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Train not found"));
        if (r.passengerName() == null || r.passengerName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passenger name required");
        if (r.seats() < 1 || r.seats() > t.availableSeats)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough seats available");
        t.availableSeats -= r.seats();
        Booking b = new Booking();
        b.pnr = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        b.trainId = t.id; b.trainName = t.name; b.route = t.fromStation + " → " + t.toStation;
        b.passengerName = r.passengerName().trim(); b.seats = r.seats(); b.totalFare = t.fare * r.seats();
        return bookings.save(b);
    }

    @GetMapping("/bookings")
    public List<Booking> myBookings() { return bookings.findAllByOrderByIdDesc(); }

    @DeleteMapping("/bookings/{id}")
    @Transactional
    public void cancel(@PathVariable Long id) {
        Booking b = bookings.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        trains.findById(b.trainId).ifPresent(t -> t.availableSeats += b.seats);
        bookings.delete(b);
    }
}
