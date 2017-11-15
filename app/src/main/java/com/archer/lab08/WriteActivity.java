package com.archer.lab08;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WriteActivity extends AppCompatActivity {

    private OutputStream outStream;
    private InputStream inStream;
    private OutputStream logOutputStream;
    private InputStream logInputStream;

    private TelephonyManager telephonyManager;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String simFileContents = null;

    public BroadcastReceiver tickReceiver;
    public BroadcastReceiver timeReceiver;
    public BroadcastReceiver bootReceiver;
    public BroadcastReceiver powerConnected;
    public BroadcastReceiver powerDisconnected;

    private Calendar calendar;

    TextView logDataField;

    public void initializeWriter() {
        try {
            outStream = new BufferedOutputStream(openFileOutput("sInfo.txt", MODE_PRIVATE));
            inStream = new BufferedInputStream(openFileInput("sInfo.txt"));
            logOutputStream = new BufferedOutputStream(openFileOutput("logdata.txt", MODE_APPEND));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializePermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void initTickReceiver() {
        tickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                calendar = Calendar.getInstance();
                String minute = Integer.toString(calendar.get(Calendar.MINUTE));
                if(calendar.get(Calendar.MINUTE) % 5 == 0) {
                    String tickData = "Time: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + minute;
                    writeLogData(tickData + "\n");
                    Toast.makeText(getApplicationContext(), tickData, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Current minute (not divisible by 5): " + minute, Toast.LENGTH_LONG).show();
                }
            }
        };
        IntentFilter tickFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(tickReceiver, tickFilter);
    }

    public void initTimeReceiver() {
        timeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                calendar = Calendar.getInstance();
                String timeChanged = "Time Changed: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                writeLogData(timeChanged + "\n");
                Toast.makeText(getApplicationContext(), timeChanged, Toast.LENGTH_LONG).show();
            }
        };
        IntentFilter timeFilter = new IntentFilter(Intent.ACTION_TIME_CHANGED);
        registerReceiver(timeReceiver, timeFilter);
    }

    public void initBootReceiver() {
        bootReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                writeLogData("Boot completed at: " + timeStamp + "\n");
                Toast.makeText(getApplicationContext(), "Boot completed at: " + timeStamp, Toast.LENGTH_LONG).show();
            }
        };
        IntentFilter bootFilter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        registerReceiver(bootReceiver, bootFilter);
    }

    public void initPowerConnected() {
        powerConnected = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(new Date());
                writeLogData("Power connected at: " + timeStamp + "\n");
                Toast.makeText(getApplicationContext(), "Power connected at: " + timeStamp, Toast.LENGTH_LONG).show();
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(powerConnected, filter);
    }

    public void initPowerDisconnected() {
        powerDisconnected = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
                int prct = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                writeLogData("Current battery percent: " + prct + "\n");
                Toast.makeText(getApplicationContext(), "Current battery percent: " + prct, Toast.LENGTH_SHORT).show();
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(powerDisconnected, filter);
    }

    public void initializeReceivers() {
        initTickReceiver();
        initTimeReceiver();
        initBootReceiver();
        initPowerConnected();
        initPowerDisconnected();
    }

    public void stopReceivers() {
        unregisterReceiver(tickReceiver);
        unregisterReceiver(timeReceiver);
        unregisterReceiver(bootReceiver);
        unregisterReceiver(powerConnected);
        unregisterReceiver(powerDisconnected);
    }

    public void writeLogData(String data) {
        try {
            logOutputStream.write(data.getBytes());
            logOutputStream.flush();
            Toast.makeText(this, "Data written", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readLogData(View view) {
        try {
            logInputStream = new BufferedInputStream(openFileInput("logdata.txt"));
            byte[] data = new byte[logInputStream.available()];
            logInputStream.read(data, 0, logInputStream.available());
            String logData = new String(data);
            logDataField.setText(logData);
            logInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeStreams() {
        try {
            logOutputStream.close();
            Toast.makeText(WriteActivity.this, "Streams are successfully closed!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeSimData(View view) throws FileNotFoundException {
        String data = "Симний мэдээлэл: \n";
        String phoneNumber = "99843328";
        String simSerial = "1234";
        String simCountry = telephonyManager.getSimCountryIso();
        String opCode = telephonyManager.getSimOperator();
        String opName = telephonyManager.getSimOperatorName();
        data += "Phone Number: " + phoneNumber + "\n SIM serial: " + simSerial + "\n";
        data += "Sim Country: " + simCountry + "\n" + "Operator Code: " + opCode + "\n Operator Name: " + opName + "\n";
        try {
            outStream.write(data.getBytes());
            Toast.makeText(this, "Амжилттай бичигдлээ.", Toast.LENGTH_LONG).show();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readSimData(View view) {
        try {
            byte[] data = new byte[inStream.available()];
            inStream.read(data, 0, inStream.available());
            simFileContents = new String(data);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, simFileContents, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        logDataField = (TextView) findViewById(R.id.logDataField);
        initializePermissions();
        initializeWriter();
        initializeReceivers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopReceivers();
        closeStreams();
    }

}
