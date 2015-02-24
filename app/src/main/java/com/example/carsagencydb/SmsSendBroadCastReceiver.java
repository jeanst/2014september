package com.example.carsagencydb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;

/**
 * Created by Jean on 18/01/2015.
 */
public class SmsSendBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("JEAN - ","HELLO FROM SENDING");
        switch (getResultCode()) {
            case ActionBarActivity.RESULT_OK:
                //Log.d("JEAN", "SMS send OK!!");
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                break;
        }

    }
}
