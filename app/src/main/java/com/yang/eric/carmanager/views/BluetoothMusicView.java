package com.yang.eric.carmanager.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseLongArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yang.eric.carmanager.BluetoothListenerActivity;
import com.yang.eric.carmanager.FloatingService;
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
    private TextView musicTitleTv;
    private TextView musicArtistTv;
    private TextView musicAlbumnTv;
    private TextView musicCountdownTv;
    private Button playBtn;
    private Handler handler = new Handler(Looper.getMainLooper());
    private float mRawX,mRawY,mStartX,mStartY;
    private boolean isStop = false;

    public BluetoothMusicView(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        mView = mLayoutInflater.inflate(R.layout.bluetooth_music_view, null);
        mWindowManager = FloatingManager.getInstance(mContext);

        musicTitleTv = mView.findViewById(R.id.music_title_tv);
        musicArtistTv = mView.findViewById(R.id.music_artist_tv);
        musicAlbumnTv = mView.findViewById(R.id.music_albumn_tv);
        musicCountdownTv = mView.findViewById(R.id.music_countdown_tv);
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
        mView.findViewById(R.id.music_listener).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, BluetoothListenerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });
        mView.findViewById(R.id.music_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, FloatingService.class);
                i.putExtra(FloatingService.ACTION, FloatingService.HIDE);
                mContext.startService(i);
                mContext.stopService(i);
                isStop = true;
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
        mView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mStartX = event.getX();
                        mStartY = event.getY();
                        Log.d(TAG, "ACTION_DOWN, mStartX: "+mStartX+", mStartY: "+mStartY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mRawX = event.getRawX();
                        mRawY = event.getRawY();
                        Log.d(TAG, "ACTION_MOVE, mRawX: "+mRawX+", mRawY: "+mRawY);
                        updateWindowPosition(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        mRawX = event.getRawX();
                        mRawY = event.getRawY();
                        Log.d(TAG, "ACTION_UP, mRawX: "+mRawX+", mRawY: "+mRawY);
                        updateWindowPosition(true);
                        break;
                }
                return true;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final String musicInfo = RsBluetoothManager.getSingleton().getMusicInfo();
                    Log.d(TAG, "musicInfo: "+musicInfo);
                    if (!TextUtils.isEmpty(musicInfo)){
                        String[] strs = musicInfo.split(RsBluetoothManager.FF_SPLIT);
                        if (strs != null && strs.length >= 4){
                            final String title = strs[0];
                            final String artist = strs[1];
                            final String albumn = strs[2];
                            int time = 0;
                            try {
                                time = Integer.valueOf(strs[3]);
                            }catch (Exception e){}
                            final int countdown = time;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (musicTitleTv != null){
                                        musicTitleTv.setText(title);
                                    }
                                    if (musicArtistTv != null){
                                        musicArtistTv.setText(artist);
                                    }
                                    if (musicAlbumnTv != null){
                                        musicAlbumnTv.setText(albumn);
                                    }
                                    if (musicCountdownTv != null){
                                        musicCountdownTv.setText(String.valueOf(countdown));
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }).start();
    }

    public void show() {
        Log.d(TAG, "show");
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.TOP | Gravity.LEFT;
        SharedPreferences sp = getSharedPreferences();
        int x = 0, y = 0;
        if (sp != null){
            x = sp.getInt("x", 0);
            y = sp.getInt("y", 0);
        }
        mParams.x = x;
        mParams.y = y;
        //总是出现在应用程序窗口之上
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.width = LayoutParams.WRAP_CONTENT;
        mParams.height = LayoutParams.WRAP_CONTENT;
        mWindowManager.addView(mView, mParams);
    }

    public void hide() {
        mWindowManager.removeView(mView);
    }

    private void updateWindowPosition(boolean isFinal) {
        if (mParams != null) {
            // 更新坐标
            mParams.x = (int)(mRawX - mStartX);
            mParams.y = (int)(mRawY - mStartY);
            // 使参数生效
            FloatingManager.getInstance(mContext).updateView(mView, mParams);
            if (isFinal){
                SharedPreferences sp = getSharedPreferences();
                if (sp != null){
                    SharedPreferences.Editor ed = sp.edit();
                    if (ed != null){
                        ed.putInt("x", mParams.x);
                        ed.putInt("y", mParams.y);
                        ed.apply();
                    }
                }
            }
        }
    }

    private SharedPreferences getSharedPreferences(){
        return mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }
}
