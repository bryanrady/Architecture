package com.bryanrady.architecture.isolation;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.architecture.isolation.bean.WeatherInfo;
import com.bryanrady.architecture.isolation.image.ImageLoaderPresenter;
import com.bryanrady.architecture.isolation.image.glide.GlideImageLoader;
import com.bryanrady.architecture.isolation.net.HttpRequestPresenter;
import com.bryanrady.architecture.isolation.net.ModelCallback;
import com.bryanrady.architecture.isolation.net.async.AsyncHttpRequest;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/6/25.
 */

public class IsolationActivity extends BaseActivity {

    @BindView(R.id.btn_isolation_http)
    Button btn_isolation_http;

    @BindView(R.id.btn_isolation_image)
    Button btn_isolation_image;

    @BindView(R.id.tv_isolation_http)
    TextView tv_isolation_http;

    @BindView(R.id.iv_isolation_image)
    ImageView iv_isolation_image;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout llBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView tvToolbarTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView tvRightTitle;

    @OnClick({R.id.ll_toolbar_back,R.id.btn_isolation_http,R.id.btn_isolation_image})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_isolation_http:
                Map<String,String> params = new HashMap<>();
                params.put("city","长沙");
                params.put("key","13cb58f5884f9749287abbead9c658f2");
                HttpRequestPresenter.getInstance()
                        .with(new AsyncHttpRequest())
                        .get("http://restapi.amap.com/v3/weather/weatherInfo", params, new ModelCallback<WeatherInfo>() {
                            @Override
                            public void onSuccess(WeatherInfo weatherInfo) {
                                tv_isolation_http.setText(weatherInfo.toString());
                            }

                            @Override
                            public void onFailure(int code, Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
                break;
            case R.id.btn_isolation_image:
                String imageUrl = "https://img01.sogoucdn.com/app/a/100520024/0aaba5bfbe2c14df4b97f19852cc531a";
                ImageLoaderPresenter.getInstance()
                        .with(new GlideImageLoader())
                        .displayImage(IsolationActivity.this,imageUrl,iv_isolation_image);

                break;
        }
    }


    @Override
    public int bindLayout() {
        return R.layout.activity_isolation;
    }

    @Override
    public void initView(View view) {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("第三方框架再隔离");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }
}
