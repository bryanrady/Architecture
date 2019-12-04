package com.bryanrady.architecture.rx.operator;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.architecture.rx.operator.transfer.BufferOperatorActivity;
import com.bryanrady.architecture.rx.operator.transfer.FlatMapOperatorActivity;
import com.bryanrady.architecture.rx.operator.transfer.GroupByOperatorActivity;
import com.bryanrady.architecture.rx.operator.transfer.MapOperatorActivity;
import com.bryanrady.architecture.rx.operator.transfer.ScanOperatorActivity;
import com.bryanrady.architecture.rx.operator.transfer.WindowOperatorActivity;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/1/9.
 */

public class TransferOperatorActivity extends BaseActivity {

    @BindView(R.id.btn_transfer_operator_map)
    Button btn_transfer_operator_map;

    @BindView(R.id.btn_transfer_operator_flatMap)
    Button btn_transfer_operator_flatMap;

    @BindView(R.id.btn_transfer_operator_groupBy)
    Button btn_transfer_operator_groupBy;

    @BindView(R.id.btn_transfer_operator_buffer)
    Button btn_transfer_operator_buffer;

    @BindView(R.id.btn_transfer_operator_scan)
    Button btn_transfer_operator_scan;

    @BindView(R.id.btn_transfer_operator_window)
    Button btn_transfer_operator_window;

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
        return R.layout.activity_operator_transfer;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    public void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("变换操作符");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_transfer_operator_map,R.id.btn_transfer_operator_flatMap,R.id.btn_transfer_operator_groupBy,
            R.id.btn_transfer_operator_buffer, R.id.btn_transfer_operator_scan,R.id.btn_transfer_operator_window})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_transfer_operator_map:
                startActivity(MapOperatorActivity.class);
                break;
            case R.id.btn_transfer_operator_flatMap:
                startActivity(FlatMapOperatorActivity.class);
                break;
            case R.id.btn_transfer_operator_groupBy:
                startActivity(GroupByOperatorActivity.class);
                break;
            case R.id.btn_transfer_operator_buffer:
                startActivity(BufferOperatorActivity.class);
                break;
            case R.id.btn_transfer_operator_scan:
                startActivity(ScanOperatorActivity.class);
                break;
            case R.id.btn_transfer_operator_window:
                startActivity(WindowOperatorActivity.class);
                break;
        }
    }

}
