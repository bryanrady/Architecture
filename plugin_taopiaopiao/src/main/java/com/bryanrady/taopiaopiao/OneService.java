package com.bryanrady.taopiaopiao;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by baby on 2018/3/6.
 */

public class OneService extends BaseService {

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(mThat, "-----服务已经开启--->", Toast.LENGTH_SHORT).show();
        new Thread(){
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    if(i==50){
                        break;
                    }
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
