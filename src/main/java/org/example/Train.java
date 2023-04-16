package org.example;

import java.util.List;

public class Train {
    private String length;
    private String type;
    private String number;
    private String brand;
    private String stationFrom;
    private String stationTo;
    private List<Place> places;
    private String timeInWay;
    private String departureTime;
    private String arrivalTime;

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setStationTo(String st) {
        this.stationTo = st;
    }

    public String getStationTo() {
        return stationTo;
    }

    public void setStationFrom(String sf) {
        this.stationFrom = sf;
    }

    public String getStationFrom() {
        return stationFrom;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public String getTimeInWay() {
        return timeInWay;
    }

    public void setTimeInWay(String timeInWay) {
        this.timeInWay = timeInWay;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }


    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        String result = "Train\n" +
                "Train length " + length + '\n' +
                "Train type " + type + '\n' +
                "Train brand " + brand + '\n' +
                "Departure station " + stationFrom + '\n' +
                "Arrival station " + stationTo + '\n' +
                "Time in way " + timeInWay + '\n' +
                "Departure Time " + departureTime + '\n' +
                "Arrival Time " + arrivalTime + '\n';
        StringBuilder sb = new StringBuilder("Places: ");
        for (int i = 0; i < this.places.size(); i++) {
            sb.append(this.places.get(i).toString());
        }
        return result + sb.toString();
    }

    public int countFreeSeats() {
        int freeSeatsNum = 0;
        for (int i = 0; i < this.places.size(); i++) {
            freeSeatsNum += places.get(i).getFreeSeats();
        }
        return freeSeatsNum;
    }
}

class Place {
    private String type;
    private int price;
    private int freeSeats;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(int freeSeats) {
        this.freeSeats = freeSeats;
    }

    @Override
    public String toString() {
        return "Type " + type + '\n' +
                "Price " + price + '\n' +
                "Free Seats " + freeSeats + '\n';
    }
}
