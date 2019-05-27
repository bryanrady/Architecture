package com.bryanrady.architecture.rx.operator.merge;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by wangqinqbin on 2019/1/10.
 */

public class ConcatOperatorActivity extends BaseActivity {

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

        tv_operator_name.setText("Concat");
        tv_operator_effect.setText("按顺序连接多个Observables。需要注意的是Observable.concat(a,b)等价于a.concatWith(b)。concat  最多连接四个");
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("合并操作符---StartWith");
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

    private void testOperator(){
        tv_send.append("Observable send : 1" + "\n");
        tv_send.append("Observable send : 2" + "\n");

        tv_send.append("Observable send : 3" + "\n");
        tv_send.append("Observable send : 4" + "\n");

        tv_send.append("Observable send : 5" + "\n");
        tv_send.append("Observable send : 6" + "\n");

        tv_send.append("Observable send : 7" + "\n");
        tv_send.append("Observable send : 8" + "\n");

        tv_send.append("Observable send : 1,2 和 3,4 和 5,6 和 7,8" + "\n");

        Observable.concat(Observable.just(1,2),Observable.just(3,4)
        ,Observable.just(5,6),Observable.just(7,8))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        tv_receive.append("Consumer receive : accept :" + integer + "\n");
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
