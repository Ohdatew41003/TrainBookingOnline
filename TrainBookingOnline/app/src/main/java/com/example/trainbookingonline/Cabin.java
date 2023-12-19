package com.example.trainbookingonline;

import java.io.Serializable;
import java.util.Map;

public class Cabin implements Serializable {
    private int number;
    private Map<String, Seat> seats;
    public Cabin() {}
    public Cabin(int number, Map<String, Seat> seats) {
        this.number = number;
        this.seats = seats;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public Map<String, Seat> getSeats() {
        return seats;
    }
    public void setSeats(Map<String, Seat> seats) {
        this.seats = seats;
    }
}