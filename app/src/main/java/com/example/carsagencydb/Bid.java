package com.example.carsagencydb;

/**
 * Created by Jean on 15/01/2015.
 */

import java.io.Serializable;

/**
 * Created by jean on 1/15/2015.
 */

public class Bid implements Serializable {
    private int bidPrice;
    private String telNum;
    private static final long serialVersionUID = 1L;

    public Bid(String tel, int price) {
        setTelNum(tel);
        setBidPrice(price);

    }

    public Bid(Bid other) {
        this(other.telNum, other.bidPrice);
    }

    public int getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(int bidPrice) {
        this.bidPrice = bidPrice > 0 ? bidPrice : 0;
    }

    public String getTelNum() {
        return new String(telNum);
    }

    public void setTelNum(String telNum) {
        this.telNum = new String(telNum);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Bid price: " + bidPrice + " Tel: " + telNum;
    }
}