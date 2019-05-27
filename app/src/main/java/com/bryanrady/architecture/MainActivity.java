package com.bryanrady.architecture;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bryanrady.architecture.aop.AopUseActivity;
import com.bryanrady.architecture.rx.RxJavaOperatorActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/1/9.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_rxJava)
    Button btn_rxJava;

    @BindView(R.id.btn_aop)
    Button btn_aop;

    @BindView(R.id.btn_dagger2)
    Button btn_dagger2;

    @BindView(R.id.btn_eventbus)
    Button btn_eventbus;

    @BindView(R.id.btn_hermes)
    Button btn_hermes;

    @BindView(R.id.btn_glide)
    Button btn_glide;

    @BindView(R.id.btn_mvp)
    Button btn_mvp;

    @BindView(R.id.btn_retrofit)
    Button btn_retrofit;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout llBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView tvToolbarTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView tvRightTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        tvToolbarTitle.setText("主流开源框架");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.btn_rxJava,R.id.btn_aop,R.id.btn_dagger2,R.id.btn_eventbus,
            R.id.btn_hermes,R.id.btn_glide,R.id.btn_mvp,R.id.btn_retrofit,})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_rxJava:
                startActivity(RxJavaOperatorActivity.class);
                break;
            case R.id.btn_aop:
                startActivity(AopUseActivity.class);
                break;
            case R.id.btn_dagger2:
                break;
            case R.id.btn_eventbus:
                break;
            case R.id.btn_hermes:
                break;
            case R.id.btn_glide:
                break;
            case R.id.btn_mvp:
                break;
            case R.id.btn_retrofit:
                break;
        }
    }
}
