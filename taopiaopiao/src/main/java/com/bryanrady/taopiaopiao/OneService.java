package com.bryanrady.taopiaopiao;

import android.content.Intent;
import android.util.Log;

/**
 * Created by baby on 2018/3/6.
 */

public class OneService extends TppBaseService {

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(){
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    Log.i("wangqingbin", "run: "+(i++));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
