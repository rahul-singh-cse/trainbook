package com.trainbook;
import jakarta.persistence.*;
@Entity
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    public String pnr, trainName, route, passengerName;
    public Long trainId;
    public int seats, totalFare;
}
