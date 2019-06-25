package com.bryanrady.architecture.eventbus;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.architecture.aop.BehaviorStatisticsActivity;
import com.bryanrady.architecture.aop.DynamicPermissionActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/6/24.
 */

public class EventBusActivity extends BaseActivity {

    @BindView(R.id.btn_event_bus_next)
    Button btn_event_bus_next;

    @BindView(R.id.tv_event_bus_info)
    TextView tv_event_bus_info;

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
        return R.layout.activity_event_bus;
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
        //https://www.jianshu.com/p/72f475ac3a8d
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receive(People people){
        tv_event_bus_info.setText("我是"+people.getName()+",今年"+people.getAge()+"岁");
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void receive(Integer msg){
        Log.d("wangqingbin","msg=="+msg);
        switch (msg.intValue()){
            case EventBusSecondActivity.CONSUME_OPERATE:
                try {
                    for (int i=1;i<6;i++){
                        Log.d("wangqingbin","i=="+i);
                        Thread.sleep(1000);
                    }
                    EventBus.getDefault().post(new Integer(EventBusSecondActivity.COMPLETE_OPERATE));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleComplete(Integer msg){
        switch (msg.intValue()){
            case EventBusSecondActivity.COMPLETE_OPERATE:
                tv_event_bus_info.setText("经过耗时处理后，展示消息");
                break;
        }

    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_event_bus_next})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_event_bus_next:
                EventBus.getDefault().postSticky(new MessageEvent("我来自第一个页面的数据"));
                startActivity(EventBusSecondActivity.class);
                break;
        }
    }
}
