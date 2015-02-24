package com.example.carsagencydb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.Serializable;

/**
 * Created by Jean on 20/01/2015.
 */
public class ListCars extends ActionBarActivity implements Serializable {

    Car[] cars;
    //CarSales agency;

    private static final long serialVersionUID = 1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_cars);

        Intent intent = getIntent();
        cars = new Car[MainActivity.agency.getNoOfCars()];
        //Toast.makeText(this, "Nr of cars " + MainActivity.agency.getNoOfCars(), Toast.LENGTH_LONG).show();
        for (int i = 0; i < MainActivity.agency.getNoOfCars(); i++) {
            cars[i] = MainActivity.agency.getCars()[i];
        }
        ImageView back = (ImageView) findViewById(R.id.back);
        final ListView listView = (ListView) findViewById(R.id.listview);
        ListAdapter adapter = new MyArrayAdapterCar(this, cars);
        listView.setAdapter(adapter);

    }

    public void btnDisplayCar(View view) {
        // Go and display it
        for (int i = 0; i < MainActivity.agency.getNoOfCars(); i++) {
            if (MainActivity.License.equals(cars[i].getLicense().toString())) {
                Intent intent = new Intent(this, DisplayOneCar.class);
                Bundle extras = new Bundle();
                //Toast.makeText(this,"Position Display " + position,Toast.LENGTH_LONG).show();
                extras.putSerializable("OneCar", (Serializable) cars[i]);
                extras.putSerializable("OneOwner", (Serializable) cars[i].getOwner());
                intent.putExtras(extras);
                startActivity(intent);
            }
        }
        //finish();
    }


    public void btnReturn(View view) {
        finish();
    }
}