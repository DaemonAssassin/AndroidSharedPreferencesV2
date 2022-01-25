package com.gmail.mateendev3.sharedpreferencesfulldetails.model;

public class Car {
    private final String carName;
    private final int carModel;

    public Car(String carName, int carModel) {
        this.carName = carName;
        this.carModel = carModel;
    }

    public String getCarName() {
        return carName;
    }

    public int getCarModel() {
        return carModel;
    }
}
