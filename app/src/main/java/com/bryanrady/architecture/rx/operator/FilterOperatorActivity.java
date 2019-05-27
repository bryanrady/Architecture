package com.bryanrady.architecture.rx.operator;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.architecture.rx.operator.filter.DistinctOperatorActivity;
import com.bryanrady.architecture.rx.operator.filter.ElementAtOperatorActivity;
import com.bryanrady.architecture.rx.operator.filter.Filter2OperatorActivity;
import com.bryanrady.architecture.rx.operator.filter.TakeOperatorActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/1/9.
 */

public class FilterOperatorActivity extends BaseActivity {

    @BindView(R.id.btn_filter_operator_filter)
    Button btn_filter_operator_filter;

    @BindView(R.id.btn_filter_operator_take)
    Button btn_filter_operator_take;

    @BindView(R.id.btn_filter_operator_distinct)
    Button btn_filter_operator_distinct;

    @BindView(R.id.btn_filter_operator_elementAt)
    Button btn_filter_operator_elementAt;

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
        return R.layout.activity_operator_filter;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    public void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("过滤操作符");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_filter_operator_filter,R.id.btn_filter_operator_take,
            R.id.btn_filter_operator_distinct, R.id.btn_filter_operator_elementAt})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_filter_operator_filter:
                startActivity(Filter2OperatorActivity.class);
                break;
            case R.id.btn_filter_operator_take:
                startActivity(TakeOperatorActivity.class);
                break;
            case R.id.btn_filter_operator_distinct:
                startActivity(DistinctOperatorActivity.class);
                break;
            case R.id.btn_filter_operator_elementAt:
                startActivity(ElementAtOperatorActivity.class);
                break;
        }
    }


}