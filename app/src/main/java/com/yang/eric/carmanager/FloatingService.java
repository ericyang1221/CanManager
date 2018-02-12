package com.yang.eric.carmanager;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

import com.yang.eric.carmanager.views.BluetoothMusicView;

/**
 * Created by erichyang on 2018/2/9.
 */

public class FloatingService extends Service {
    public static final String ACTION = "action";
    public static final String SHOW = "show";
    public static final String HIDE = "hide";
    public static final String STOP = "stop";
    private BluetoothMusicView bluetoothMusicView;

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothMusicView = new BluetoothMusicView(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getStringExtra(ACTION);
            if (SHOW.equals(action)) {
                bluetoothMusicView.show();
            } else if (HIDE.equals(action)) {
                bluetoothMusicView.hide();
            }
            if (STOP.equals(action)) {
                stopSelf();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}