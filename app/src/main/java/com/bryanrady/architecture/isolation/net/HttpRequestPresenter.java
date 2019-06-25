package com.bryanrady.architecture.isolation.net;

import com.bryanrady.architecture.isolation.net.okhttp.OkhttpHttpRequest;

import java.util.Map;

/**
 * Created by Administrator on 2019/6/25.
 */

public class HttpRequestPresenter implements HttpRequest {

    private HttpRequest mHttpRequest;
    private static volatile HttpRequestPresenter instance;

    private HttpRequestPresenter(){
        mHttpRequest = new OkhttpHttpRequest();
    }

    public static HttpRequestPresenter getInstance(){
        if (instance == null){
            synchronized (HttpRequestPresenter.class){
                if (instance == null){
                    instance = new HttpRequestPresenter();
                }
            }
        }
        return instance;
    }

    public HttpRequestPresenter with(HttpRequest httpRequest) {
        this.mHttpRequest = httpRequest;
        return instance;
    }

    @Override
    public void get(String url, Map<String, String> params, ICallback callback) {
        if(mHttpRequest == null){
            //你必须通过with方法先制定请求类型
            throw new IllegalStateException("You must first formulate the request type (HttpRequest) through the with method!");
        }
        mHttpRequest.get(url,params,callback);
    }

    @Override
    public void post(String url, Map<String, String> params, ICallback callback) {
        if(mHttpRequest == null){
            throw new IllegalStateException("You must first formulate the request type through the with method!");
        }
        mHttpRequest.post(url,params,callback);
    }
}
