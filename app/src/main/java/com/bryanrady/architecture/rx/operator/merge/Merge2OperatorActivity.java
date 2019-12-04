package com.bryanrady.architecture.rx.operator.merge;

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

/**
 * Created by wangqinqbin on 2019/1/10.
 */

public class Merge2OperatorActivity extends BaseActivity {

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

        tv_operator_name.setText("Merge");
        tv_operator_effect.setText("将多个Observable合并为一个。不同于concat，merge不是按照添加顺序连接，" +
                "而是按照时间线来连接。其中mergeDelayError将异常延迟到其它没有错误的Observable发送完毕后才发射。" +
                "而merge则是一遇到异常将停止发射数据，发送onError通知。");
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("合并操作符---Merge");
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

        tv_send.append("Observable send : 5" + "\n");
        tv_send.append("Observable send : 6" + "\n");
        tv_send.append("Observable send : 7" + "\n");

        tv_send.append("Observable send : 0" + "\n");
        tv_send.append("Observable send : 0" + "\n");
        tv_send.append("Observable send : 0" + "\n");

        tv_send.append("Observable send : 1,2,3 和 5,6,7和 0，0，0" + "\n");

        Observable observable1=Observable.just(1,2,3);

        Observable observable2=Observable.just(5,6,7);

        Observable observable3=Observable.just(0,0,0);

        Observable.merge(observable1,observable3, observable2)
                .subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                tv_receive.append("Consumer receive : accept :" + o + "\n");
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
