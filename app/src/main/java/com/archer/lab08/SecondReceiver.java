package com.archer.lab08;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SecondReceiver extends BroadcastReceiver {

    private final static String RECEIVE_SMS = "RECEIVE_SMS";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!intent.getAction().equals(RECEIVE_SMS))
            return;

        switch (getResultCode()) {
            case AppCompatActivity.RESULT_OK:
                Toast.makeText(context, "Мессеж амжилттай илгээгдлээ!", Toast.LENGTH_LONG).show();
                break;
            case AppCompatActivity.RESULT_CANCELED:
                Toast.makeText(context, "Илгээлт амжилтгүй!", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
