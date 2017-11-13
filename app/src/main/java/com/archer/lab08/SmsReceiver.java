package com.archer.lab08;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import static com.archer.lab08.MainActivity.appendMessage;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;

        String str = "";
        if(bundle != null) {

            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            for(int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += "Ирсэн дугаар: " + msgs[i].getOriginatingAddress() + " \n";
                str += "Текст мессеж: " + msgs[i].getMessageBody().toString() + " \n";
            }
            appendMessage(str);
            Toast.makeText(context, "Ирсэн мессеж: \n" + str, Toast.LENGTH_LONG).show();
        }
    }
}
