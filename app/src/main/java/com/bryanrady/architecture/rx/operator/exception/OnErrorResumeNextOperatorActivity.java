package com.bryanrady.architecture.rx.operator.exception;

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
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by wangqinqbin on 2019/1/10.
 */

public class OnErrorResumeNextOperatorActivity extends BaseActivity {

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

        tv_operator_name.setText("OnErrorResumeNext");
        tv_operator_effect.setText("遇到错误时，发送1个新的Observable");

//        注：
//
//        onErrorResumeNext（）拦截的错误 = Throwable；若需拦截Exception请用onExceptionResumeNext（）
//        若onErrorResumeNext（）拦截的错误 = Exception，则会将错误传递给观察者的onError方法

    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("异常处理操作符---OnErrorResumeNext");
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
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Throwable("出现错误"));
                tv_send.append("Observable send : 1" + "\n");
                tv_send.append("Observable send : 2" + "\n");
                tv_send.append("Observable send : 出现错误" + "\n");
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> apply(Throwable throwable) throws Exception {
                tv_send.append("Observable send :出现错误后重新发送 11" + "\n");
                tv_send.append("Observable send :出现错误后重新发送 22" + "\n");
                return Observable.just(11,22);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                tv_receive.append("Observer receive : onNext " + integer.intValue() + "\n");
            }

            @Override
            public void onError(Throwable e) {
                tv_receive.append("Observable receive : onError " + e.getMessage() + "\n");
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
