package com.yang.eric.carmanager;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yang.eric.carmanager.tools.TXZConfigManager;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    private final int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private TextView consoleTv;
    private static Handler handler = new Handler(Looper.getMainLooper());

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
        consoleTv = findViewById(R.id.console);
        testCode();
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

    private void testCode(){
        final StringBuffer sb = new StringBuffer();

        sb.append("-------------------------------------------\n");

        sb.append("-------------------------------------------\n");
        handler.post(new Runnable() {
            @Override
            public void run() {
                consoleTv.setText(sb);
            }
        });
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                consoleTv.setText(event.toString()+keyCode);
            }
        });
        return super.onKeyDown(keyCode, event);
    }
}
