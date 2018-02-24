package com.yang.eric.carmanager.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by erichyang on 2018/2/13.
 */

public class KeyEventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.KEY_EVENT")) {

        }
    }
}
