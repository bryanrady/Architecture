package com.bryanrady.architecture.rx.operator.transfer;

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
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by wangqinqbin on 2019/1/10.
 */

public class MapOperatorActivity extends BaseActivity {

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

        tv_operator_name.setText("Map");
        tv_operator_effect.setText("Map操作符对Observable发射的每一项数据都应用一个函数来变换");
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("转换型操作符---Map");
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
        Observable.just(1,2,3)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        //可以通过map操作符来进行网络请求
                        return integer.intValue()+"1";
                    }
                }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(String s) {
                tv_receive.append("Observer receive : onNext " + s + "\n");
            }

            @Override
            public void onError(Throwable e) {
                tv_receive.append("Observer receive : onError " + e.getMessage() + "\n");
            }

            @Override
            public void onComplete() {
                tv_receive.append("Observer receive : onComplete " + "\n");
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
