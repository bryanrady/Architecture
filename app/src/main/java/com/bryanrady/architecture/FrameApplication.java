package com.bryanrady.architecture;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by Administrator on 2019/1/3.
 */

public class FrameApplication extends Application {

    private static final String TAG = "FrameApplication";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d(TAG,"attachBaseContext");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


}
