package com.example.carsagencydb;

/**
 * Created by Jean on 21/01/2015.
 */

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Hackeru on 1/11/2015.
 */
public class MyArrayAdapterCar extends ArrayAdapter<Car> {
    Activity context;
    Car[] cars;

    public MyArrayAdapterCar(Activity context, Car[] cars) {
        super(context, R.layout.list_cars, R.id.manufacturer, cars);
        this.context = context;
        this.cars = cars;
    }

    static class ViewContainer {
        public TextView manufacturer;
        public TextView carnumber;
        public TextView year;
        public TextView km;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewContainer viewContainer;
        View rowView = convertView;


//
// Reusing views
//
        if (rowView == null) {
            Log.d("JEAN", "We are in a FRESH getView, position " + position);
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_cars, null, true);
            viewContainer = new ViewContainer();
            viewContainer.manufacturer = (Button) rowView.findViewById(R.id.manufacturer);
            viewContainer.carnumber = (TextView) rowView.findViewById(R.id.carnumber);
            viewContainer.year = (TextView) rowView.findViewById(R.id.year);
            viewContainer.km = (TextView) rowView.findViewById(R.id.km);
            rowView.setTag(viewContainer);
        } else {
            viewContainer = (ViewContainer) rowView.getTag();
            Log.d("JEAN", "We are in a REUSED getView, position " + position);
        }

        viewContainer.manufacturer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int p = Integer.valueOf(v.getTag().toString());
                Toast.makeText(getContext(),
                        "Car: " + cars[p].getLicense(), Toast.LENGTH_LONG).show();
                MainActivity.License = cars[p].getLicense();
                MainActivity.enteredcarnumber.setText(MainActivity.License);
            }
        });

        viewContainer.manufacturer.setText(cars[position].getManufacturer());
        viewContainer.carnumber.setText(cars[position].getLicense());
        String syear = "";
        String skm = "";
        syear += (cars[position].getYear());
        skm += (cars[position].getKm());
        viewContainer.year.setText(syear);
        viewContainer.km.setText(skm);
        //viewContainer.manufacturer.setBackgroundColor(R.color.bleu);
        viewContainer.manufacturer.setTag(position);

        return rowView;
    }

}


