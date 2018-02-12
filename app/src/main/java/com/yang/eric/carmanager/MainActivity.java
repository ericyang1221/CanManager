package com.yang.eric.carmanager;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.yang.eric.carmanager.tools.TXZConfigManager;

public class MainActivity extends AppCompatActivity {
    private final int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intent = new Intent(this, FloatingService.class);
        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra(FloatingService.ACTION, FloatingService.SHOW);
                startService(intent);
            }
        });
        findViewById(R.id.btn_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra(FloatingService.ACTION, FloatingService.HIDE);
                startService(intent);
            }
        });
        findViewById(R.id.btn_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        findViewById(R.id.btn_txzicon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TXZConfigManager.getInstance().showFloatTool(0);
            }
        });
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN://按向下键
                Toast.makeText(this, "KEYCODE_DPAD_DOWN", Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:// 按向上键
                Toast.makeText(this, "KEYCODE_DPAD_UP", Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT://按向左键
                Toast.makeText(this, "KEYCODE_DPAD_LEFT", Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT://按向右键
                Toast.makeText(this, "KEYCODE_DPAD_RIGHT", Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER://按向右键
                Toast.makeText(this, "KEYCODE_DPAD_CENTER", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
