package com.bryanrady.remoteprocess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Administrator on 2019/5/31.
 */

public class MyAppService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new MyAppProxy();
    }
}
