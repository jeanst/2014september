package com.example.carsagencydb;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jean on 22/01/2015.
 */
public class MyArrayAdapterOwner extends ArrayAdapter<Owner> {
    Activity context;
    Owner[] owners;

    public MyArrayAdapterOwner(Activity context, Owner[] owners) {
        super(context, R.layout.list_owners, R.id.ownername, owners);
        this.context = context;
        this.owners = owners;
    }

    static class ViewContainer {
        public TextView ownername;
        public TextView telephone;

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
            rowView = inflater.inflate(R.layout.list_owners, null, true);
            viewContainer = new ViewContainer();
            viewContainer.ownername = (TextView) rowView.findViewById(R.id.ownername);
            viewContainer.telephone = (TextView) rowView.findViewById(R.id.telephone);

            rowView.setTag(viewContainer);
        } else {
            viewContainer = (ViewContainer) rowView.getTag();
            Log.d("JEAN", "We are in a REUSED getView, position " + position);
        }

        viewContainer.ownername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = Integer.valueOf(v.getTag().toString());
                Toast.makeText(MyArrayAdapterOwner.this.context,
                        owners[position] + " " + position, Toast.LENGTH_LONG).show();
            }
        });

        viewContainer.ownername.setText(owners[position].getName());
        viewContainer.telephone.setText(owners[position].getTelNumber());
        viewContainer.ownername.setTag(position);
        return rowView;
    }
}
