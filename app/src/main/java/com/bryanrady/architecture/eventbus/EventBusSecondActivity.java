package com.bryanrady.architecture.eventbus;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/6/24.
 */

public class EventBusSecondActivity extends BaseActivity {

    @BindView(R.id.btn_event_bus_next_send_main)
    Button btn_event_bus_next_send_main;

    @BindView(R.id.btn_event_bus_next_send_background)
    Button btn_event_bus_next_send_background;

    @BindView(R.id.tv_event_bus_next_info)
    TextView tv_event_bus_next_info;

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
        return R.layout.activity_event_bus_second;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("EventBus");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void receive(MessageEvent event){
        tv_event_bus_next_info.setText(event.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_event_bus_next_send_main,R.id.btn_event_bus_next_send_background})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_event_bus_next_send_main:
                EventBus.getDefault().post(new People("Lucy","女",21));
                defaultFinish();
                break;
            case R.id.btn_event_bus_next_send_background:
                EventBus.getDefault().post(new Integer(CONSUME_OPERATE));
                defaultFinish();
                break;
        }
    }

    public static final int CONSUME_OPERATE = 10086;
    public static final int COMPLETE_OPERATE = 10087;
}
