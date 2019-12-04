package com.bryanrady.architecture.rx.operator;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;
import com.bryanrady.architecture.rx.operator.merge.ConcatOperatorActivity;
import com.bryanrady.architecture.rx.operator.merge.Merge2OperatorActivity;
import com.bryanrady.architecture.rx.operator.merge.StartWithOperatorActivity;
import com.bryanrady.architecture.rx.operator.merge.XXDelayErrorOperatorActivity;
import com.bryanrady.architecture.rx.operator.merge.ZipOperatorActivity;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/1/9.
 */

public class MergeOperatorActivity extends BaseActivity {

    @BindView(R.id.btn_merge_operator_startWith)
    Button btn_merge_operator_startWith;

    @BindView(R.id.btn_merge_operator_concat)
    Button btn_merge_operator_concat;

    @BindView(R.id.btn_merge_operator_merge)
    Button btn_merge_operator_merge;

    @BindView(R.id.btn_merge_operator_xxMapDelayError)
    Button btn_merge_operator_xxMapDelayError;

    @BindView(R.id.btn_merge_operator_zip)
    Button btn_merge_operator_zip;

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
        return R.layout.activity_operator_merge;
    }

    @Override
    public void initView(View view) {
        initToolbar();
    }

    public void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("合并操作符");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.ll_toolbar_back,R.id.btn_merge_operator_startWith,R.id.btn_merge_operator_concat,
            R.id.btn_merge_operator_merge, R.id.btn_merge_operator_xxMapDelayError,R.id.btn_merge_operator_zip})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_merge_operator_startWith:
                startActivity(StartWithOperatorActivity.class);
                break;
            case R.id.btn_merge_operator_concat:
                startActivity(ConcatOperatorActivity.class);
                break;
            case R.id.btn_merge_operator_merge:
                startActivity(Merge2OperatorActivity.class);
                break;
            case R.id.btn_merge_operator_xxMapDelayError:
                startActivity(XXDelayErrorOperatorActivity.class);
                break;
            case R.id.btn_merge_operator_zip:
                startActivity(ZipOperatorActivity.class);
                break;
        }
    }


}