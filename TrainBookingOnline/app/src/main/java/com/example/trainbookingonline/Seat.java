package com.example.trainbookingonline;

import java.io.Serializable;

public class Seat implements Serializable {
    private int seatNumber;
    private int price;
    private String status;
    private String cabin;
    public Seat() {}

    public Seat(int seatNumber, int price, String status, String cabin) {
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = status;
        this.cabin = cabin;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
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
