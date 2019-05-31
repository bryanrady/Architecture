package com.bryanrady.remoteprocess;

import android.os.RemoteException;
import android.util.Log;

import com.bryanrady.architecture.MyAppAIDL;


/**
 * 这是MyAppService服务(相对客户端来说 这是个远端服务)的提供给客户端
 * Created by Administrator on 2019/5/31.
 */

public class MyAppProxy extends MyAppAIDL.Stub {

    private String mName;

    @Override
    public void setName(String name) throws RemoteException {
        Log.d("wangqingbin","client set name == "+ name);
        mName = name;
    }

    @Override
    public String getName() throws RemoteException {
        Log.d("wangqingbin","get service mName == "+mName);
        return mName;
    }
}
