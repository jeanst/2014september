package com.example.carsagencydb;

/**
 * Created by Jean on 15/01/2015.
 */
/**
 * Created by jean on 1/15/2015.
 */

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Car implements Serializable {
    private static final int MAX_KM = 20000;
    private static final int KM_PER_YEAR = 12000;
    private Owner owner;
    private String manufacturer;
    private String license;
    private boolean airbags;
    private boolean leasingOrRental;
    private int km, seats, year;
    private int price;
    private int LevyPrice;
    private Bid highestBid;
    private Bitmap image;
    private static final long serialVersionUID = 1L;

    public Car(Owner owner, String license, String manufacturer, boolean airbags, boolean leasingOrRental,
               int year, int seats, int km, int price, int LevyPrice, Bid highestBid) {
        this.owner = new Owner(owner);
        this.license = license;
        this.manufacturer = new String(manufacturer);
        this.airbags = airbags;
        this.leasingOrRental = leasingOrRental;
        this.year = year;
        this.seats = seats;
        this.km = km;
        this.price = price;
        this.LevyPrice = LevyPrice;
        this.highestBid = new Bid(highestBid);
        this.image = image;
    }

    public Car(Owner owner, String license, String manufacturer, boolean airbags, boolean leasingOrRental,
               int year, int seats, int km, int price, int LevyPrice) {
        this(owner, license, manufacturer, airbags, leasingOrRental, year, seats, km, price, LevyPrice, new Bid("", 0));
    }

    public Car(Car other) {
        this(other.owner, other.license, other.manufacturer, other.airbags, other.leasingOrRental,
                other.year, other.seats, other.km, other.price, other.LevyPrice, other.highestBid);
    }


    public Owner getOwner() {
        return new Owner(owner);
    }

    public String getLicense() {
        return new String(license);
    }

    public String getManufacturer() {
        return new String(manufacturer);
    }

    public boolean hasAirbags() {
        return airbags;
    }

    public boolean isLeasingOrRental() {
        return leasingOrRental;
    }

    public int getYear() {
        return year;
    }

    public int getSeats() {
        return seats;
    }

    public int getKm() {
        return km;
    }

    public int getPrice() {
        return price;
    }

    public int getLevyPrice() {
        return LevyPrice;
    }

    public Bid getHighestBid() {
        return new Bid(highestBid);
    }


    public void makeBid(Bid bid) {
        if (bid.getBidPrice() > highestBid.getBidPrice())
            highestBid = new Bid(bid);
    }

    public boolean isAttractive() {

        boolean notTooHighKM = km <= MAX_KM;
        boolean notTooOld = age() < +3;
        return airbags && notTooHighKM && !leasingOrRental && notTooOld;
    }

    public boolean fitForFamily(int numberOfKids) {
        return numberOfKids + 2 <= seats;
    }

    public int age() {
        GregorianCalendar gcalendar = new GregorianCalendar();
        return gcalendar.get(Calendar.YEAR) - year + 1;
    }

    public boolean overUsedCar() {
        return km > age() * KM_PER_YEAR;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String s = "Manufacturer: " + manufacturer + "\n";
        s += "License Number: " + license + "\n";
        s += "Year: " + year + "\n";
        s += "Km: " + km + "\n";
        s += "Owner: " + owner.toString() + "\n";
        s += "Highest bid: " + highestBid.getBidPrice();
        return s;
    }
}
