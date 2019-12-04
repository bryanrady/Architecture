package com.bryanrady.architecture.rx.operator.filter;

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
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangqinqbin on 2019/1/10.
 */

public class DistinctOperatorActivity extends BaseActivity {

    @BindView(R.id.tv_operator_name)
    TextView tv_operator_name;

    @BindView(R.id.tv_operator_effect)
    TextView tv_operator_effect;

    @BindView(R.id.btn_doSomething)
    Button btn_doSomething;

    @BindView(R.id.tv_send)
    TextView tv_send;

    @BindView(R.id.tv_receive)
    TextView tv_receive;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout llBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView tvToolbarTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView tvRightTitle;

    private Disposable disposable;

    @Override
    public int bindLayout() {
        return R.layout.activity_operator_base;
    }

    @Override
    public void initView(View view) {
        initToolbar();

        tv_operator_name.setText("Distinct");
        tv_operator_effect.setText("过滤重复数据，内部通过OperatorDistinct实现。");
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("过滤型操作符---Distinct");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
    }

    @Override
    public void doBusiness(Context context) {}

    @OnClick({R.id.ll_toolbar_back,R.id.btn_doSomething})
    void onClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.btn_doSomething:
                testOperator();
                break;
        }
    }

   // distinctUntilChanged 过滤掉连续重复的数据

    private void testOperator(){
        tv_send.append("Observable send : 1" + "\n");
        tv_send.append("Observable send : 1" + "\n");
        tv_send.append("Observable send : 1" + "\n");
        tv_send.append("Observable send : 3" + "\n");
        tv_send.append("Observable send : 3" + "\n");
        tv_send.append("Observable send : 4" + "\n");
        tv_send.append("Observable send : 5" + "\n");
        Observable.just(1,1,1,3,3,4,5)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        tv_receive.append("Consumer receive : accept " + integer + "\n");
                    }
                });

        tv_send.append("Observable send : 1" + "\n");
        tv_send.append("Observable send : 1" + "\n");
        tv_send.append("Observable send : 1" + "\n");
        tv_send.append("Observable send : 3" + "\n");
        tv_send.append("Observable send : 3" + "\n");
        tv_send.append("Observable send : 4" + "\n");
        tv_send.append("Observable send : 5" + "\n");
        Observable.just(1,1,1,3,3,4,5)
                .distinct(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return 1;
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        tv_receive.append("Consumer receive : accept " + integer + "\n");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
