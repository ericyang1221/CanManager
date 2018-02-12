package com.yang.eric.carmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by erichyang on 2018/2/11.
 */

public class BluetoothListenerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置1像素
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN://按向下键
                Toast.makeText(this, "next", Toast.LENGTH_SHORT).show();
                RsBluetoothManager.getSingleton().nextMusic();
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:// 按向上键
                Toast.makeText(this, "previous", Toast.LENGTH_SHORT).show();
                RsBluetoothManager.getSingleton().preMusic();
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER://按向右键
                Toast.makeText(this, "KEYCODE_DPAD_CENTER", Toast.LENGTH_SHORT).show();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
