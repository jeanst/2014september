package com.example.carsagencydb;

/**
 * Created by Jean on 15/01/2015.
 */

import java.io.Serializable;

/**
 * Created by jean on 1/15/2015.
 */


public class CarSales implements Serializable {
    private Car[] cars;
    private int noOfCars;

    private static final long serialVersionUID = 1L;

    public CarSales(int size) {
        cars = new Car[size];
        noOfCars = 0;
    }

    public boolean addCar(Car c) {
        if (noOfCars < getCars().length) {
            getCars()[noOfCars++] = c;
            return true;
        }
        return false;
    }

    public Car getNewest() {
        if (noOfCars == 0)
            return null;
        Car newestCar = getCars()[0];
        int highestYear = getCars()[0].getYear();
        for (int i = 1; i < noOfCars; i++) {
            if (getCars()[i].getYear() > highestYear)
                newestCar = getCars()[i];
        }
        return new Car(newestCar);
    }

    public String allAttractive() {
        String s = "";
        for (int i = 0; i < noOfCars; i++)
            if (getCars()[i].isAttractive())
                s += getCars()[i].toString() + (i == noOfCars - 1 ? "" : "\n\n");
        return s;
    }

    public String ownersOfType(String manufacturer) {
        String s = "";
        for (int i = 0; i < noOfCars; i++)
            if (getCars()[i].getManufacturer().equals(manufacturer))
                s += getCars()[i].getOwner() + (i == noOfCars - 1 ? "" : "\n\n");
        return s;
    }

    public void makeBidsOnCheapNotOverUsed(String tel, int maxPrice) {
        for (Car c : getCars()) {
            int nextPrice = c.getHighestBid().getBidPrice() + 100;
            if (!c.overUsedCar() && nextPrice <= maxPrice)
                c.makeBid(new Bid(tel, nextPrice));
        }
    }

    public int getNoOfCars() {
        return noOfCars;
    }

    public Car[] getCars() {
        return cars;
    }

}
