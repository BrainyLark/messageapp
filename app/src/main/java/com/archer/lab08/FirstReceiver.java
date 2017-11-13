package com.archer.lab08;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.Toast;

public class FirstReceiver extends BroadcastReceiver {

    private final static String SENT_SMS = "SENT_SMS";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!intent.getAction().equals(SENT_SMS))
            return;

        switch (getResultCode()) {
            case AppCompatActivity.RESULT_OK:
                Toast.makeText(context, "Амжилттай сүлжээгээр гарлаа!", Toast.LENGTH_LONG).show();
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Toast.makeText(context, "Сүлжээ алга байна", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(context, "Илгээлт амжилтгүй боллоо.", Toast.LENGTH_LONG).show();
        }
    }
}
