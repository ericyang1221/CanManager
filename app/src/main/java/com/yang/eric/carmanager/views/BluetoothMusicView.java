package com.yang.eric.carmanager.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yang.eric.carmanager.R;
import com.yang.eric.carmanager.RsBluetoothManager;
import com.yang.eric.carmanager.tools.FloatingManager;

/**
 * Created by erichyang on 2018/2/9.
 */

public class BluetoothMusicView extends FrameLayout {
    private final String TAG = "BluetoothMusicView";
    private Context mContext;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private FloatingManager mWindowManager;
    private TextView musicInfoTv;
    private Button playBtn;
    private Handler handler = new Handler(Looper.getMainLooper());

    public BluetoothMusicView(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        mView = mLayoutInflater.inflate(R.layout.bluetooth_music_view, null);
        mWindowManager = FloatingManager.getInstance(mContext);

        musicInfoTv = mView.findViewById(R.id.music_info_tv);
        mView.findViewById(R.id.music_next).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RsBluetoothManager.getSingleton().nextMusic();
            }
        });
        mView.findViewById(R.id.music_previous).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RsBluetoothManager.getSingleton().preMusic();
            }
        });
        playBtn = mView.findViewById(R.id.music_play);
        if(RsBluetoothManager.getSingleton().isMusicPlaying()){
            playBtn.setText("pause");
        }else{
            playBtn.setText("play");
        }
        playBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(RsBluetoothManager.getSingleton().isMusicPlaying()){
                    RsBluetoothManager.getSingleton().muteMusic();
                    RsBluetoothManager.getSingleton().pauseMusic();
                    playBtn.setText("play");
                }else{
                    RsBluetoothManager.getSingleton().muteMusic();
                    RsBluetoothManager.getSingleton().playMusic();
                    playBtn.setText("pause");
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (Boolean.TRUE){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final String musicInfo = RsBluetoothManager.getSingleton().getMusicInfo();
                    Log.d(TAG, "musicInfo: "+musicInfo);
                    if (!TextUtils.isEmpty(musicInfo)){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (musicInfoTv != null){
                                    musicInfoTv.setText(musicInfo);
                                }
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void show() {
        Log.d(TAG, "show");
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        mParams.x = 0;
        mParams.y = 100;
        //总是出现在应用程序窗口之上
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.width = LayoutParams.WRAP_CONTENT;
        mParams.height = LayoutParams.WRAP_CONTENT;
        mWindowManager.addView(mView, mParams);
    }

    public void hide() {
        mWindowManager.removeView(mView);
    }
}
