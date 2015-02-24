package com.example.carsagencydb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Jean on 24/01/2015.
 */
@SuppressLint("ValidFragment")
public class AddCarDialog extends ActionBarActivity implements Serializable {
    Car c1;
    Owner o1;

    private static final long serialVersionUID = 1L;


    EditText carmodel;
    EditText caryear;
    EditText carnumber;
    EditText ownername;
    EditText ownertel;
    EditText nrseats;
    EditText km;
    EditText price;
    EditText levyprice;
    //Button btnDone;
    ImageView back;
    RadioButton btnyes1, btnyes2, btnno1, btnno2;

    String Model;
    String License;
    String OwnerName;
    String OwnerTel;

    int Year;
    int Km;
    int Seats;
    int Price;
    int LevyPrice;

    Boolean AirBags = false;
    Boolean LisingRental = false;

    GregorianCalendar gcalendar;
    int count = 0;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car);

        //Intent intent = getIntent();


        gcalendar = new GregorianCalendar();

        //btnDone = (Button) findViewById(R.id.btnDone);
        carmodel = (EditText) findViewById(R.id.carmodel);
        carnumber = (EditText) findViewById(R.id.carnumber);
        ownername = (EditText) findViewById(R.id.ownername);
        ownertel = (EditText) findViewById(R.id.ownertel);
        caryear = (EditText) findViewById(R.id.caryear);
        nrseats = (EditText) findViewById(R.id.nrseats);
        km = (EditText) findViewById(R.id.km);
        price = (EditText) findViewById(R.id.price);
        levyprice = (EditText) findViewById(R.id.levyprice);
        btnyes1 = (RadioButton) findViewById(R.id.btnyes1);
        btnno1 = (RadioButton) findViewById(R.id.btnno1);
        btnyes1.setOnClickListener(btnListener);
        btnno1.setOnClickListener(btnListener);
        btnyes2 = (RadioButton) findViewById(R.id.btnyes2);
        btnno2 = (RadioButton) findViewById(R.id.btnno2);
        btnyes2.setOnClickListener(btnListener);
        btnno2.setOnClickListener(btnListener);
        back = (ImageView) findViewById(R.id.back);

    }

    public void onClick(View view) {
        count = 0;
        if (carmodel.getText().toString().length() > 0) {
            Model = carmodel.getText().toString();
            count++;
        }
        if (carnumber.getText().toString().length() > 0) {
            License = carnumber.getText().toString();
            count++;
        }
        if (ownername.getText().toString().length() > 0) {
            OwnerName = ownername.getText().toString();
            count++;
        }
        if (ownertel.getText().toString().length() > 0) {
            OwnerTel = ownertel.getText().toString();
            count++;
        }
        if ((caryear.getText().toString().length()) > 0) {
            Year = Integer.parseInt(caryear.getText().toString());
            count++;
        }
        if ((km.getText().toString().length()) > 0) {
            Km = Integer.parseInt(km.getText().toString());
            count++;
        }
        if ((nrseats.getText().toString().length()) > 0) {
            Seats = Integer.parseInt(nrseats.getText().toString());
            count++;
        }
        if ((price.getText().toString().length()) > 0) {
            Price = Integer.parseInt(price.getText().toString());
            count++;
        }
        if ((levyprice.getText().toString().length()) > 0) {
            LevyPrice = Integer.parseInt(levyprice.getText().toString());
            count++;
        }
        if (count == 9)
            checkValuesEntered();
        if (count != 9) {
            Toast.makeText(this, "Check all the values !!!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "The new car will be registred!!!", Toast.LENGTH_LONG).show();
            o1 = new Owner(OwnerName, OwnerTel);
            c1 = new Car(o1, License, Model, AirBags, LisingRental, Year, Seats, Km, Price, LevyPrice);
        }

    }

    private void checkValuesEntered() {
        if (Year < 1900 || Year > gcalendar.get(Calendar.YEAR)) {
            Toast.makeText(this, "Year entered is not legal " + Year, Toast.LENGTH_LONG).show();
            caryear.setText("0000");
            count--;
        }
        if (Seats < 2 || Seats > 7) {
            Toast.makeText(this, "Seats entered is not an usual value " + Seats, Toast.LENGTH_LONG).show();
            nrseats.setText("0000");
            count--;
        }

        if ((License.indexOf('-') != 2) || (License.lastIndexOf('-') != 6)) {
            Toast.makeText(this, "License number not legal  " + License, Toast.LENGTH_LONG).show();
            carnumber.setText("00-000-00");
            count--;
        }
        for (int i = 0; i < MainActivity.agency.getNoOfCars(); i++) {
            if (License.equals(MainActivity.agency.getCars()[i].getLicense())) {
                Toast.makeText(this, "License number already in Agency  " + License, Toast.LENGTH_LONG).show();
                count--;
            }
        }
        if ((OwnerTel.indexOf('-') != 3)) {
            Toast.makeText(this, "Telephon number not legal  " + OwnerTel, Toast.LENGTH_LONG).show();
            ownertel.setText("05X-1234567");
            count--;
        }
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String s = "";
            boolean isclicked = ((RadioButton) v).isChecked();
            // Check which radio button was clicked
            switch (v.getId()) {
                case R.id.btnyes1:
                    if (isclicked)
                        LisingRental = true;
                    break;
                case R.id.btnno1:
                    if (isclicked)
                        LisingRental = false;
                    break;
                case R.id.btnyes2:
                    if (isclicked)
                        AirBags = true;
                    break;
                case R.id.btnno2:
                    if (isclicked)
                        AirBags = false;
                    break;
            }
        }
    };

    public void btnReturn(View view) {
        //
        // Return back the added car & owner !!!
        //
        if (count != 9) {
            Toast.makeText(this, "No new car added", Toast.LENGTH_LONG).show();
            Intent data = new Intent();
            setResult(RESULT_CANCELED);
            finish();
        }

        Intent data = new Intent();
        Bundle extras = new Bundle();
        extras.putSerializable("AddedCar", c1);
        data.putExtras(extras);
        setResult(RESULT_OK, data);
        finish();
    }
}
