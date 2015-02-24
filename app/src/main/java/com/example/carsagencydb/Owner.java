package com.example.carsagencydb;

/**
 * Created by Jean on 15/01/2015.
 */

import java.io.Serializable;

/**
 * Created by jean on 1/15/2015.
 */


public class Owner implements Serializable {
    private String name, telNumber;
    private static final long serialVersionUID = 1L;

    public Owner(String name, String tel) {
        this.name = new String(name);
        this.telNumber = new String(tel);
    }

    public Owner(Owner other) {
        this(other.name, other.telNumber);
    }

    public String getTelNumber() {
        //return new String(telNumber);
        return new String(telNumber);
    }

    public String getName() {
        //return new String(name);
        return new String(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj instanceof Owner) {
            Owner o = (Owner) obj;
            return this.name.equals(o.name) && this.telNumber.equals(o.telNumber);
        }
        return false;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Owner Name: " + getName() + " Tel: " + getTelNumber();
    }
}