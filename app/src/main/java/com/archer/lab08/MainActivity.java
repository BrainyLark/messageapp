package com.archer.lab08;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText sendNumberField;
    private EditText textSmsField;
    private static TextView resultField;
    private SmsManager sm;
    private PendingIntent sentSms;
    private PendingIntent dSms;

    private final static String SENT_SMS = "SENT_SMS";
    private final static String RECEIVE_SMS = "RECEIVE_SMS";
    private static final int PERMISSION_REQUEST_CODE = 1;

    public void initializePermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void initializeViews() {
        sendNumberField = (EditText) findViewById(R.id.sendNumberField);
        textSmsField = (EditText) findViewById(R.id.textSmsField);
        resultField = (TextView) findViewById(R.id.resultField);
    }

    public void initializeTelephony() {
        sm = SmsManager.getDefault();
        sentSms = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(SENT_SMS), 0);
        dSms = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(RECEIVE_SMS), 0);
    }

    public void sendMessage(View view) {
        String sendNumber = sendNumberField.getText().toString();
        String textMsg = textSmsField.getText().toString();
        //Toast.makeText(this, sendNumber + ": " + textMsg, Toast.LENGTH_LONG).show();
        sm.sendTextMessage("+976" + sendNumber, null, textMsg, sentSms, dSms);
    }

    public static void appendMessage(String sendText) {
        resultField.append("\n" + sendText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializePermissions();
        initializeViews();
        initializeTelephony();

        Toast.makeText(this, "Шаардлагатай тохиргоонуудыг бэлтгэж дууслаа!", Toast.LENGTH_LONG).show();
    }
}
