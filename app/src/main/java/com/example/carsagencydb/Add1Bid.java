package com.example.carsagencydb;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Created by Jean on 30/01/2015.
 */
public class Add1Bid extends ActionBarActivity implements Serializable {
    Car c1;
    Owner o1;
    Bid bid;
    private static final long serialVersionUID = 1L;

    TextView carmodel;
    TextView caryear;
    TextView carnumber;
    TextView ownername;
    TextView ownertel;

    TextView km;
    TextView price;
    TextView levyprice;
    TextView leasing;
    TextView airbag;
    EditText newbid;
    EditText newtel;
    TextView answer;
    String Model;
    String License;
    String OwnerName;
    String OwnerTel;

    int Year;
    int Km;
    int Seats;
    int Price;
    int LevyPrice;
    int newBid;
    //Button btnDone;
    ImageView back;
    Button btnyes;
    Button btnno;
    private boolean sendSMS = false;
    int posit;
    int count = -1;
    GregorianCalendar gcalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bid);
        bid = new Bid("", 0);
        Intent intent = getIntent();
        posit = MainActivity.position;
        c1 = MainActivity.agency.getCars()[posit];
        o1 = c1.getOwner();

        gcalendar = new GregorianCalendar();

        //btnDone = (Button) findViewById(R.id.btnDone);
        carmodel = (TextView) findViewById(R.id.carmodel);
        carnumber = (TextView) findViewById(R.id.carnumber);
        ownername = (TextView) findViewById(R.id.ownername);
        ownertel = (TextView) findViewById(R.id.ownertel);
        caryear = (TextView) findViewById(R.id.caryear);

        km = (TextView) findViewById(R.id.km);
        price = (TextView) findViewById(R.id.price);
        levyprice = (TextView) findViewById(R.id.levyprice);
        leasing = (TextView) findViewById(R.id.leasing);
        //airbag = (TextView)findViewById(R.id.airbag);
        newbid = (EditText) findViewById(R.id.newbid);
        newtel = (EditText) findViewById(R.id.newtel);
        answer = (TextView) findViewById(R.id.answer);
        back = (ImageView) findViewById(R.id.back);
        carmodel.setText(c1.getManufacturer());
        carnumber.setText(c1.getLicense());
        ownername.setText(o1.getName());
        ownertel.setText(o1.getTelNumber());
        String syear = "";
        syear = syear + c1.getYear();
        caryear.setText(syear);

        String skm = "";
        skm = skm + c1.getKm();
        km.setText(skm);
        String sprice = "";
        sprice = sprice + c1.getPrice();
        price.setText(sprice);
        String slevy = "";
        slevy = slevy + c1.getLevyPrice();
        levyprice.setText(slevy);
        btnyes = (RadioButton) findViewById(R.id.btnyes);
        btnno = (RadioButton) findViewById(R.id.btnno);
        btnyes.setOnClickListener(btnListener);
        btnno.setOnClickListener(btnListener);


    }

    public void getBid(View view) {
        count = -1;
        String sbid = "";
        String newTel = "";
        if (newbid.getText().toString().length() > 0) {
            sbid = newbid.getText().toString();
            newBid = Integer.valueOf(sbid);
            count++;
        } else {
            Toast.makeText(getBaseContext(), "Your BID is not OK" + sbid, Toast.LENGTH_LONG).show();
            newbid.setText("0");
        }
        if (newtel.getText().toString().length() > 0) {
            newTel = newtel.getText().toString();
            if ((newTel.indexOf('-') != 3)) {
                Toast.makeText(this, "Telephon number not legal  " + newTel, Toast.LENGTH_LONG).show();
                newtel.setText("05X-1234567");
            } else {
                count++;
            }
        }
        if (count == 1) {
            if (newBid > MainActivity.agency.getCars()[posit].getHighestBid().getBidPrice()) {
                answer.setText("YOUR BID IS ACCEPTED.");
                bid.setBidPrice(newBid);
                bid.setTelNum(newTel);
                MainActivity.agency.getCars()[posit].makeBid(bid);
                sendSMS = true;
                DBAdapter dbadapter = new DBAdapter(this);
                dbadapter.open();
                dbadapter.updateCar(c1);
                dbadapter.close();
            } else {
                answer.setText("Your BID Is Too Low !!");
                sendSMS = false;
            }
        }
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String s = "";
            boolean isclicked = ((RadioButton) v).isChecked();
            // Check which radio button was clicked
            switch (v.getId()) {
                case R.id.btnyes:
                    if (isclicked && sendSMS) {
                        sendSMStoOwner();
                    } else {
                        answer.setText("NO NEED TO SEND SMS!!");
                    }
                    break;
                case R.id.btnno:
                    if (isclicked)
                        break;
            }
        }
    };


    public void sendSMStoOwner() {

        SmsManager sms = SmsManager.getDefault();
        PendingIntent pendingIntentSent = PendingIntent.getBroadcast(this,
                345, new Intent("SMS_SENT"), 0);
        PendingIntent pendingIntentDeliver = PendingIntent.getBroadcast(this,
                346, new Intent("SMS_DELIVERED"), 0);
        String message = "A new bid: " + newBid + " for your car.";
        sms.sendTextMessage(o1.getTelNumber(),
                null, message, pendingIntentSent, pendingIntentDeliver);
        Toast.makeText(this, "A SMS has been sent!!", Toast.LENGTH_LONG).show();
    }


    public void btnReturn(View view) {
        // Return back the added the new bid!!!
        //
        if (count != 1) {
            Toast.makeText(this, "No new bid added", Toast.LENGTH_LONG).show();
            //return;
        }
        finish();
    }

}
