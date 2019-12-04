package com.bryanrady.architecture.rx.operator;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.architecture.rx.operator.create.Create2OperatorActivity;
import com.bryanrady.architecture.rx.operator.create.DeferOperatorActivity;
import com.bryanrady.architecture.rx.operator.create.EmptyOperatorActivity;
import com.bryanrady.architecture.rx.operator.create.FromArrayOperatorActivity;
import com.bryanrady.architecture.rx.operator.create.IntervalOperatorActivity;
import com.bryanrady.architecture.rx.operator.create.JustOperatorActivity;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangqingbin on 2019/1/9.
 */

public class CreateOperatorActivity extends BaseActivity {

    @BindView(R.id.btn_create_operator_create)
    Button btn_create_operator_create;

    @BindView(R.id.btn_create_operator_just)
    Button btn_create_operator_just;

    @BindView(R.id.btn_create_operator_fromArray)
    Button btn_create_operator_fromArray;

    @BindView(R.id.btn_create_operator_emty)
    Button btn_create_operator_emty;

    @BindView(R.id.btn_create_operator_defer)
    Button btn_create_operator_defer;

    @BindView(R.id.btn_create_operator_interval)
    Button btn_create_operator_interval;

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
        return R.layout.activity_operator_create;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    public void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("创建操作符");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_create_operator_create,R.id.btn_create_operator_just,R.id.btn_create_operator_fromArray,
            R.id.btn_create_operator_emty, R.id.btn_create_operator_defer,R.id.btn_create_operator_interval})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_create_operator_create:
                startActivity(Create2OperatorActivity.class);
                break;
            case R.id.btn_create_operator_just:
                startActivity(JustOperatorActivity.class);
                break;
            case R.id.btn_create_operator_fromArray:
                startActivity(FromArrayOperatorActivity.class);
                break;
            case R.id.btn_create_operator_emty:
                startActivity(EmptyOperatorActivity.class);
                break;
            case R.id.btn_create_operator_defer:
                startActivity(DeferOperatorActivity.class);
                break;
            case R.id.btn_create_operator_interval:
                startActivity(IntervalOperatorActivity.class);
                break;
        }
    }


}
