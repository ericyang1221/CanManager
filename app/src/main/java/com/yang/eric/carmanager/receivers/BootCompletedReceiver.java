package com.yang.eric.carmanager.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by erichyang on 2018/2/12.
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.w("boot_broadcast_poc", "starting service...");
    }

}
