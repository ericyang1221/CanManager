package com.yang.eric.carmanager.tools;

import java.lang.reflect.Method;

/**
 * Created by erichyang on 2018/2/12.
 */

public class TXZConfigManager {
    private static class TXZConfigManagerHolder{
        private static TXZConfigManager INSTANCE = new TXZConfigManager();
    }

    private TXZConfigManager(){

    }

    public static TXZConfigManager getInstance(){
        return TXZConfigManagerHolder.INSTANCE;
    }

    public void showFloatTool(int value){
        try{
            Class<?> clazz = Class.forName("com.txznet.sdk.TXZConfigManager");
            Method getSingletonMethod = clazz.getMethod("getInstance");
            Object instance = getSingletonMethod.invoke(null);
            Method method = clazz.getMethod("showFloatTool", int.class);
            method.invoke(instance, value);
        }catch (Exception e){
        }
    }
}
