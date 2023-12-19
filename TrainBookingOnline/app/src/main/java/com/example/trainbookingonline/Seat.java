package com.example.trainbookingonline;

import java.io.Serializable;

public class Seat implements Serializable {
    private int seatNumber;
    private int price;
    private String status;
    public Seat() {}

    public Seat(int seatNumber, int price, String status) {
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
