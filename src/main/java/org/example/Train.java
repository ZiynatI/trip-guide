package org.example;

import java.util.List;

public class Train {

    private final int length;
    private final String type;
    private final String number;
    private final String brand;
    private final String stationFrom;
    private final String stationTo;
    private final List<Place> places;
    private final String timeInWay;
    private final String departureTime;
    private final String arrivalTime;

    public Train(int length, String type, String number, String brand,
                 String stationFrom, String stationTo, List<Place> places,
                 String timeInWay, String departureTime, String arrivalTime) {
        this.length = length;
        this.type = type;
        this.number = number;
        this.brand = brand;
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.places = places;
        this.timeInWay = timeInWay;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        String result = "______Train______\n" +
                "Train length " + length + '\n' +
                "Train type " + type + '\n' +
                "Train number " + number + '\n' +
                "Train brand " + brand + '\n' +
                "Departure station " + stationFrom + '\n' +
                "Arrival station " + stationTo + '\n' +
                "Time in way " + timeInWay + '\n' +
                "Departure Time " + departureTime + '\n' +
                "Arrival Time " + arrivalTime + '\n';
        StringBuilder sb = new StringBuilder("Places: ");
        for (Place place : this.places) {
            sb.append(place.toString());
        }
        return result + sb;
    }

    public int countFreeSeats() {
        int freeSeatsNum = 0;
        for (Place place : this.places) {
            freeSeatsNum += place.getFreeSeats();
        }
        return freeSeatsNum;
    }
}

class Place {
    private final String type;
    private final int price;
    private final int freeSeats;

    public Place(String type, int price, int freeSeats) {
        this.type = type;
        this.price = price;
        this.freeSeats = freeSeats;
    }

    public int getFreeSeats() {
        return freeSeats;
    }

    @Override
    public String toString() {
        return "Type " + type + '\n' +
                "Price " + price + '\n' +
                "Free Seats " + freeSeats + '\n';
    }
}
