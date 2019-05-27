package com.bryanrady.architecture.rx;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.architecture.rx.operator.ConditionOperatorActivity;
import com.bryanrady.architecture.rx.operator.CreateOperatorActivity;
import com.bryanrady.architecture.rx.operator.ExceptionOperatorActivity;
import com.bryanrady.architecture.rx.operator.FilterOperatorActivity;
import com.bryanrady.architecture.rx.operator.MergeOperatorActivity;
import com.bryanrady.architecture.rx.operator.TransferOperatorActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * https://www.jianshu.com/p/b30de498c3cc
 * Created by wangqingbin on 2019/1/9.
 */

public class RxJavaOperatorActivity extends BaseActivity {

    @BindView(R.id.btn_operator_crate)
    Button btn_operator_crate;

    @BindView(R.id.btn_operator_transfer)
    Button btn_operator_transfer;

    @BindView(R.id.btn_operator_filter)
    Button btn_operator_filter;

    @BindView(R.id.btn_operator_merge)
    Button btn_operator_merge;

    @BindView(R.id.btn_operator_condition)
    Button btn_operator_condition;

    @BindView(R.id.btn_operator_exception)
    Button btn_operator_exception;

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
        return R.layout.activity_rxjava_operator;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("RxJava六大类型操作符");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_operator_crate,R.id.btn_operator_transfer,R.id.btn_operator_filter,
            R.id.btn_operator_merge, R.id.btn_operator_condition,R.id.btn_operator_exception})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_operator_crate:
                startActivity(CreateOperatorActivity.class);
                break;
            case R.id.btn_operator_transfer:
                startActivity(TransferOperatorActivity.class);
                break;
            case R.id.btn_operator_filter:
                startActivity(FilterOperatorActivity.class);
                break;
            case R.id.btn_operator_merge:
                startActivity(MergeOperatorActivity.class);
                break;
            case R.id.btn_operator_condition:
                startActivity(ConditionOperatorActivity.class);
                break;
            case R.id.btn_operator_exception:
                startActivity(ExceptionOperatorActivity.class);
                break;
        }
    }

}
