package com.yang.eric.carmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yang.eric.carmanager.tools.RsCanBusManager;
import com.yang.eric.carmanager.tools.TXZConfigManager;

public class MainActivity extends AppCompatActivity {
    private final int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private TextView consoleTv;
    private static Handler handler = new Handler(Looper.getMainLooper());
    private IntentFilter mFilter;
    private int count;
    private AudioManager audioManager;
    private String lastKey;
    private boolean isListenCarInfo;
    private TextView speedTv, mileageTv, oilTv;
    private BroadcastReceiver keyEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            final String action = intent.getAction();
            final String key;
            if ("android.intent.action.KEY_EVENT".equals(action)) {
                key = intent.getStringExtra("key");
            } else if ("android.intent.action.TIMEZONE_CHANGED".equals(action)) {
                key = intent.getStringExtra("time-zone");
            } else if ("com.android.rs.car_dynamic_info".equals(action)) {
                key = intent.getStringExtra("data");
            } else if ("com.rs.air_on_off".equals(action)) {
                key = intent.getStringExtra("air_on_off");
            } else if ("com.rs.air_info".equals(action)) {
                key = intent.getStringExtra("air_info");
            } else {
                key = "null";
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (consoleTv != null && !key.equals(lastKey)) {
                        consoleTv.append("action: " + action + ", key: " + key + ", bundle: "+printBundles(intent) + "\n");
                    }
                    lastKey = key;
                }
            });
        }
    };

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
                playSound();
            }
        });
        consoleTv = findViewById(R.id.console);
        speedTv = findViewById(R.id.speed);
        mileageTv = findViewById(R.id.mileage);
        oilTv = findViewById(R.id.oil);
        testCode();
        mFilter = new IntentFilter();
        mFilter.addAction("android.intent.action.TIME_TICK");
        mFilter.addAction("android.intent.action.TIME_SET");
        mFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        mFilter.addAction("com.rs.bluetooth.ACTION_CONNET_STATE_CHANGE");
        mFilter.addAction("android.intent.action.KEY_EVENT");
        mFilter.addAction("com.android.rs.car_dynamic_info");
        mFilter.addAction("com.rs.air_on_off");
        mFilter.addAction("com.rs.air_info");
        registerReceiver(keyEventReceiver, this.mFilter);
    }

    @Override
    public void onResume(){
        super.onResume();
        isListenCarInfo = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isListenCarInfo){
                    final int speed = RsCanBusManager.getSingleton().reqCarSpeed();
                    final int mileage = RsCanBusManager.getSingleton().reqMileage();
                    final int oil = RsCanBusManager.getSingleton().reqOil();
                    Log.d("CarInfo", "speed: "+speed+", mileage: "+mileage+", oil: "+oil);
                    if (handler != null){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (speedTv != null){
                                    speedTv.setText(String.valueOf(speed));
                                }
                                if (mileageTv != null){
                                    mileageTv.setText(String.valueOf(mileage));
                                }
                                if (oilTv != null){
                                    oilTv.setText(String.valueOf(oil));
                                }
                            }
                        });
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onPause(){
        super.onPause();
        isListenCarInfo = false;
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

    private void testCode() {
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
                consoleTv.append(event.toString() + keyCode + "\n");
            }
        });
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(keyEventReceiver);
    }

    private void playSound() {
        if (audioManager == null) {
            audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        }
        int type = count % 3;
        String name;
        if (type == 0) {
            name = "receiver";
            changeToReceiver();
        } else if (type == 1) {
            name = "headset";
            changeToHeadset();
        } else {
            name = "speaker";
            changeToSpeaker();
        }
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        final int id = soundPool.load(this, R.raw.dontpanic, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(id, 1, 1, 0, 0, 1);
            }
        });
        count++;
    }

    /**
     * 切换到听筒
     */
    private void changeToReceiver() {
        audioManager.setSpeakerphoneOn(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }

    /**
     * 切换到耳机模式
     */
    private void changeToHeadset() {
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.stopBluetoothSco();
        audioManager.setBluetoothScoOn(false);
        audioManager.setSpeakerphoneOn(false);
    }

    /**
     * 切换到外放
     */
    public void changeToSpeaker() {
        //注意此处，蓝牙未断开时使用MODE_IN_COMMUNICATION而不是MODE_NORMAL
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.stopBluetoothSco();
        audioManager.setBluetoothScoOn(false);
        audioManager.setSpeakerphoneOn(true);
    }

    private String printBundles(Intent intent) {
        if (intent == null) {
            return "";
        }
        Bundle bundle = intent.getExtras();
        StringBuffer sb = new StringBuffer();
        for (String key : bundle.keySet()) {
            sb.append("[Key=" + key + ", content=" + bundle.getString(key)+"]");
        }
        return sb.toString();
    }
}
