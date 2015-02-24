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
 * Created by Jean on 22/01/2015.
 */

public class ListOwners extends ActionBarActivity implements Serializable {
    Owner[] owners;
    //Car[] cars;
    CarSales agency;

    private static final long serialVersionUID = 1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_owners);

        Intent intent = getIntent();

        owners = new Owner[MainActivity.agency.getNoOfCars()];
        for (int i = 0; i < MainActivity.agency.getNoOfCars(); i++) {
            owners[i] = MainActivity.agency.getCars()[i].getOwner();
        }
        ImageView back = (ImageView) findViewById(R.id.back);
        final ListView listView = (ListView) findViewById(R.id.listviewowners);
        ListAdapter adapter = new MyArrayAdapterOwner(this, owners);
        listView.setAdapter(adapter);

    }

    public void btnReturn(View view) {

        finish();
    }

}
