package com.yang.eric.carmanager.tools;

import java.lang.reflect.Method;

/**
 * Created by erichyang on 2018/2/24.
 */

public class RsCanBusManager {
    private static class RsCanBusManagerHolder {
        private static RsCanBusManager INSTANCE = new RsCanBusManager();
    }

    private RsCanBusManager() {

    }

    public static RsCanBusManager getSingleton() {
        return RsCanBusManagerHolder.INSTANCE;
    }

    public int reqCarSpeed() {
        int ret = 0;
        try {
            Class<?> clazz = Class.forName("com.rs.core.canbus.RsCanBusManager");
            Method getSingletonMethod = clazz.getMethod("getSingleton");
            Object instance = getSingletonMethod.invoke(null);
            Method method = clazz.getMethod("ReqCarSpeed");
            ret = (int) method.invoke(instance);
        } catch (Exception e) {
        }
        return ret;
    }

    public int reqMileage() {
        int ret = 0;
        try {
            Class<?> clazz = Class.forName("com.rs.core.canbus.RsCanBusManager");
            Method getSingletonMethod = clazz.getMethod("getSingleton");
            Object instance = getSingletonMethod.invoke(null);
            Method method = clazz.getMethod("ReqMileage");
            ret = (int) method.invoke(instance);
        } catch (Exception e) {
        }
        return ret;
    }

    public int reqOil(){
        int ret = 0;
        try {
            Class<?> clazz = Class.forName("com.rs.core.canbus.RsCanBusManager");
            Method getSingletonMethod = clazz.getMethod("getSingleton");
            Object instance = getSingletonMethod.invoke(null);
            Method method = clazz.getMethod("ReqOil");
            ret = (int) method.invoke(instance);
        } catch (Exception e) {
        }
        return ret;
    }
}
