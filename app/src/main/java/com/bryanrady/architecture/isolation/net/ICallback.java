package com.bryanrady.architecture.isolation.net;

/**
 * Created by Administrator on 2019/6/25.
 */

public interface ICallback {

    void onSuccess(String response);

    void onFailure(int code ,Throwable throwable);

}
