package com.archer.lab08;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by root on 11/14/17.
 */

public class MiddlewareActivity extends TabActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabHost tabHost = getTabHost();

        tabHost.addTab(tabHost.newTabSpec("First").setIndicator("Мессеж бичих").setContent(new Intent(this, MainActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("Second").setIndicator("Файлд бичих").setContent(new Intent(this, WriteActivity.class)));
        tabHost.setCurrentTab(0);
    }
}
