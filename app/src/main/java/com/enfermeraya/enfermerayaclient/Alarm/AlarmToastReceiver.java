package com.enfermeraya.enfermerayaclient.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by reale on 24/11/2016.
 */

public class AlarmToastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context," ALARMA",Toast.LENGTH_LONG).show();
    }
}
