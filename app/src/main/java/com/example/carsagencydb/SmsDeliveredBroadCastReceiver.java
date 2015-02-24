package com.example.carsagencydb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Jean on 18/01/2015.
 */
public class SmsDeliveredBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("JEAN - ","HELLO FROM DELIVERY");
        switch (getResultCode()) {
            case ActionBarActivity.RESULT_OK:
                //Log.d("JEAN", "SMS DELIVERED OK!!");
                break;
            case ActionBarActivity.RESULT_CANCELED:
                //Log.d("JEAN", "SMS CANCELLED");
                break;
        }

    }
}
