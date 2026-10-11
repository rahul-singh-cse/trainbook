package com.trainbook;
import jakarta.persistence.*;
@Entity
public class Train {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    public String name, fromStation, toStation, departure, arrival;
    public int fare, availableSeats;
    public Train() {}
    public Train(String name, String from, String to, String dep, String arr, int fare, int seats) {
        this.name = name; fromStation = from; toStation = to; departure = dep; arrival = arr; this.fare = fare; availableSeats = seats;
    }
}
