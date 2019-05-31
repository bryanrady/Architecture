package com.bryanrady.remoteprocess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2019/5/31.
 */

public class MyAppService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyAppProxy();
    }
}
