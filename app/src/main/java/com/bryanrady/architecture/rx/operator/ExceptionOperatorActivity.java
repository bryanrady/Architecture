package com.bryanrady.architecture.rx.operator;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.architecture.rx.operator.exception.OnErrorResumeNextOperatorActivity;
import com.bryanrady.architecture.rx.operator.exception.OnErrorReturnOperatorActivity;
import com.bryanrady.architecture.rx.operator.exception.OnExceptionResumeNextOperatorActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangqingbin on 2019/1/9.
 */

public class ExceptionOperatorActivity extends BaseActivity {

    @BindView(R.id.btn_exception_operator_onErrorReturn)
    Button btn_exception_operator_onErrorReturn;

    @BindView(R.id.btn_exception_operator_onExceptionResumeNext)
    Button btn_exception_operator_onExceptionResumeNext;

    @BindView(R.id.btn_exception_operator_onErrorResumeNext)
    Button btn_exception_operator_onErrorResumeNext;

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
        return R.layout.activity_operator_exception;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    public void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("异常操作符");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_exception_operator_onErrorReturn,
            R.id.btn_exception_operator_onExceptionResumeNext,R.id.btn_exception_operator_onErrorResumeNext})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_exception_operator_onErrorReturn:
                startActivity(OnErrorReturnOperatorActivity.class);
                break;
            case R.id.btn_exception_operator_onExceptionResumeNext:
                startActivity(OnExceptionResumeNextOperatorActivity.class);
                break;
            case R.id.btn_exception_operator_onErrorResumeNext:
                startActivity(OnErrorResumeNextOperatorActivity.class);
                break;
        }
    }


}
