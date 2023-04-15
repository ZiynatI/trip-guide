package org.example;

public class Train {
    private String length;
    private String type;
    private String number;
    private String brand;
    private Route route;
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

    public Route getRoute() {
        return route;
    }
    public void setRoute(Route route) {
        this.route = route;
    }
}

class Route {
    private String[] station;
}