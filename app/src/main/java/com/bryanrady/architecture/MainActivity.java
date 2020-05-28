package com.bryanrady.architecture;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanrady.architecture.aop.AopUseActivity;
import com.bryanrady.architecture.databind.DataBindingUseActivity;
import com.bryanrady.architecture.eventbus.EventBusActivity;
import com.bryanrady.architecture.ioc.IocActivity;
import com.bryanrady.architecture.isolation.IsolationActivity;
import com.bryanrady.architecture.plugin.PluginActivity;
import com.bryanrady.architecture.rx.RxJavaOperatorActivity;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/1/9.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_plugin)
    Button btn_plugin;

    @BindView(R.id.btn_okHttp)
    Button btn_okHttp;

    @BindView(R.id.btn_retrofit)
    Button btn_retrofit;

    @BindView(R.id.btn_rxJava)
    Button btn_rxJava;

    @BindView(R.id.btn_glide)
    Button btn_glide;

    @BindView(R.id.btn_eventbus)
    Button btn_eventbus;

    @BindView(R.id.btn_dagger2)
    Button btn_dagger2;

    @BindView(R.id.btn_mvp)
    Button btn_mvp;

    @BindView(R.id.btn_data_binding)
    Button btn_data_binding;

    @BindView(R.id.btn_mvvm)
    Button btn_mvvm;

    @BindView(R.id.btn_aop)
    Button btn_aop;

    @BindView(R.id.btn_hermes)
    Button btn_hermes;

    @BindView(R.id.btn_ioc)
    Button btn_ioc;

    @BindView(R.id.btn_butterknife)
    Button btn_butterknife;

    @BindView(R.id.btn_isolation)
    Button btn_isolation;

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

    @OnClick({R.id.btn_plugin,R.id.btn_okHttp,R.id.btn_retrofit,R.id.btn_rxJava,R.id.btn_glide,R.id.btn_eventbus,
            R.id.btn_dagger2,R.id.btn_mvp, R.id.btn_data_binding,R.id.btn_mvvm,R.id.btn_aop,R.id.btn_hermes,
            R.id.btn_ioc, R.id.btn_butterknife,R.id.btn_isolation})
    void onClick(View view){
        switch (view.getId()){
            case R.id.btn_plugin:
                startActivity(PluginActivity.class);
                break;
            case R.id.btn_okHttp:
                Toast.makeText(this,"OkHttp暂时没开通！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_retrofit:
                Toast.makeText(this,"Retrofit暂时没开通！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_rxJava:
                startActivity(RxJavaOperatorActivity.class);
                break;
            case R.id.btn_glide:
                Toast.makeText(this,"Glide暂时没开通！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_eventbus:
                startActivity(EventBusActivity.class);
                break;
            case R.id.btn_dagger2:
                Toast.makeText(this,"Dagger2暂时没开通！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_mvp:
                Toast.makeText(this,"Mvp暂时没开通！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_data_binding:
                startActivity(new Intent(this, DataBindingUseActivity.class));
                break;
            case R.id.btn_mvvm:
                Toast.makeText(this,"Mvvm暂时没开通！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_aop:
                startActivity(AopUseActivity.class);
                break;
            case R.id.btn_hermes:
                Toast.makeText(this,"Hermes暂时没开通！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_ioc:
                startActivity(IocActivity.class);
                break;
            case R.id.btn_butterknife:
                Toast.makeText(this,"ButterKnife暂时没开通！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_isolation:
                startActivity(IsolationActivity.class);
                break;
        }
    }
}
