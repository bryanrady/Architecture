package com.bryanrady.architecture.isolation.net;

import java.util.Map;

/**
 * Created by Administrator on 2019/6/25.
 */

public interface HttpRequest {

    void get(String url, Map<String,String> params, ICallback callback);

    void post(String url, Map<String,String> params, ICallback callback);

}
