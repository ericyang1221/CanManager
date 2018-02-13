package com.yang.eric.carmanager.tools;

import java.lang.reflect.Method;

/**
 * Created by erichyang on 2018/2/9.
 */

public class RsBluetoothManager {
    public static final String FF_SPLIT = new String(new byte[]{(byte) -1});
    private static class RsBluetoothManagerHolder{
        private static RsBluetoothManager INSTANCE = new RsBluetoothManager();
    }

    private RsBluetoothManager(){

    }

    public static RsBluetoothManager getSingleton(){
        return RsBluetoothManagerHolder.INSTANCE;
    }


    public void setBluetoothVolume(String type, int value){
        try{
            Class<?> clazz = Class.forName("com.rs.core.bluetooth.RsBluetoothManager");
            Method getSingletonMethod = clazz.getMethod("getSingleton");
            Object instance = getSingletonMethod.invoke(null);
            Method method = clazz.getMethod("reqVolume", String.class, int.class);
            method.invoke(instance, type, value);
        }catch (Exception e){
        }
    }

    public String getMusicInfo(){
        String ret = null;
        try{
            Class<?> clazz = Class.forName("com.rs.core.bluetooth.RsBluetoothManager");
            Method getSingletonMethod = clazz.getMethod("getSingleton");
            Object instance = getSingletonMethod.invoke(null);
            Method method = clazz.getMethod("getMusicInfo");
            ret = (String)method.invoke(instance);
        }catch (Exception e){
        }
        return ret;
    }

    public void muteMusic(){
        try{
            Class<?> clazz = Class.forName("com.rs.core.bluetooth.RsBluetoothManager");
            Method getSingletonMethod = clazz.getMethod("getSingleton");
            Object instance = getSingletonMethod.invoke(null);
            Method method = clazz.getMethod("muteMusic");
            method.invoke(instance);
        }catch (Exception e){
        }
    }

    public void playMusic(){
        try{
            Class<?> clazz = Class.forName("com.rs.core.bluetooth.RsBluetoothManager");
            Method getSingletonMethod = clazz.getMethod("getSingleton");
            Object instance = getSingletonMethod.invoke(null);
            Method method = clazz.getMethod("playMusic");
            method.invoke(instance);
        }catch (Exception e){
        }
    }

    public void pauseMusic(){
        try{
            Class<?> clazz = Class.forName("com.rs.core.bluetooth.RsBluetoothManager");
            Method getSingletonMethod = clazz.getMethod("getSingleton");
            Object instance = getSingletonMethod.invoke(null);
            Method method = clazz.getMethod("pauseMusic");
            method.invoke(instance);
        }catch (Exception e){
        }
    }

    public void preMusic(){
        try{
            Class<?> clazz = Class.forName("com.rs.core.bluetooth.RsBluetoothManager");
            Method getSingletonMethod = clazz.getMethod("getSingleton");
            Object instance = getSingletonMethod.invoke(null);
            Method method = clazz.getMethod("preMusic");
            method.invoke(instance);
        }catch (Exception e){
        }
    }

    public void nextMusic(){
        try{
            Class<?> clazz = Class.forName("com.rs.core.bluetooth.RsBluetoothManager");
            Method getSingletonMethod = clazz.getMethod("getSingleton");
            Object instance = getSingletonMethod.invoke(null);
            Method method = clazz.getMethod("nextMusic");
            method.invoke(instance);
        }catch (Exception e){
        }
    }

    public boolean isMusicPlaying(){
        boolean isMusicPlaying = false;
        try{
            Class<?> clazz = Class.forName("com.rs.core.bluetooth.RsBluetoothManager");
            Method getSingletonMethod = clazz.getMethod("getSingleton");
            Object instance = getSingletonMethod.invoke(null);
            Method method = clazz.getMethod("isMusicPlaying");
            isMusicPlaying = (boolean)method.invoke(instance);
        }catch (Exception e){
        }
        return isMusicPlaying;
    }
}
