package com.bryanrady.architecture.aop;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2019/2/26.
 */

public class AopUseActivity extends BaseActivity {

    @BindView(R.id.btn_aop_behavior_statistics)
    Button btn_aop_behavior_statistics;

    @BindView(R.id.btn_aop_dynamic_permission_apply)
    Button btn_aop_dynamic_permission_apply;

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
        return R.layout.activity_aop_use;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("Aop切面编程");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_aop_behavior_statistics,R.id.btn_aop_dynamic_permission_apply})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_aop_behavior_statistics:
                startActivity(BehaviorStatisticsActivity.class);
                break;
            case R.id.btn_aop_dynamic_permission_apply:
                startActivity(DynamicPermissionActivity.class);
                break;
        }
    }

}