package com.bryanrady.architecture.utils;

import android.util.Log;

/**
 * Created by pengyu520 on 2016/6/21.
 * Log统一管理类
 */
public class LogUtil {

    public static boolean isDebug = true;   // 是否需要打印bug，可以在application的onCreate函数里面初始化

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }
}
