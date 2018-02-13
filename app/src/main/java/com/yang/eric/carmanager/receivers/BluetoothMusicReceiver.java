package com.yang.eric.carmanager.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yang.eric.carmanager.FloatingService;

/**
 * Created by erichyang on 2018/2/12.
 */

public class BluetoothMusicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra("status").equalsIgnoreCase("play")) {
            Intent i = new Intent(context, FloatingService.class);
            i.putExtra(FloatingService.ACTION, FloatingService.SHOW);
            context.startService(i);
        } else {

        }
    }
}
