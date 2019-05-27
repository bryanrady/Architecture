package com.bryanrady.architecture.rx.operator;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.architecture.rx.operator.condition.AllOperatorActivity;
import com.bryanrady.architecture.rx.operator.condition.ContainsOperatorActivity;
import com.bryanrady.architecture.rx.operator.condition.DefaultIfEmptyOperatorActivity;
import com.bryanrady.architecture.rx.operator.condition.IsEmptyOperatorActivity;
import com.bryanrady.architecture.rx.operator.condition.RepeatUntilOperatorActivity;
import com.bryanrady.architecture.rx.operator.condition.SkipWhileOperatorActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/1/9.
 */

public class ConditionOperatorActivity extends BaseActivity {

    @BindView(R.id.btn_condition_operator_all)
    Button btn_condition_operator_all;

    @BindView(R.id.btn_condition_operator_contains)
    Button btn_condition_operator_contains;

    @BindView(R.id.btn_condition_operator_repeatUntil)
    Button btn_condition_operator_repeatUntil;

    @BindView(R.id.btn_condition_operator_isEmpty)
    Button btn_condition_operator_isEmpty;

    @BindView(R.id.btn_condition_operator_defaultIfEmpty)
    Button btn_condition_operator_defaultIfEmpty;

    @BindView(R.id.btn_condition_operator_skipWhile)
    Button btn_condition_operator_skipWhile;

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
        return R.layout.activity_operator_condition;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    public void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("条件操作符");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_condition_operator_all,R.id.btn_condition_operator_contains,R.id.btn_condition_operator_repeatUntil,
            R.id.btn_condition_operator_isEmpty, R.id.btn_condition_operator_defaultIfEmpty,R.id.btn_condition_operator_skipWhile})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_condition_operator_all:
                startActivity(AllOperatorActivity.class);
                break;
            case R.id.btn_condition_operator_contains:
                startActivity(ContainsOperatorActivity.class);
                break;
            case R.id.btn_condition_operator_repeatUntil:
                startActivity(RepeatUntilOperatorActivity.class);
                break;
            case R.id.btn_condition_operator_isEmpty:
                startActivity(IsEmptyOperatorActivity.class);
                break;
            case R.id.btn_condition_operator_defaultIfEmpty:
                startActivity(DefaultIfEmptyOperatorActivity.class);
                break;
            case R.id.btn_condition_operator_skipWhile:
                startActivity(SkipWhileOperatorActivity.class);
                break;
        }
    }


}
