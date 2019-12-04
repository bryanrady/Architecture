package com.bryanrady.architecture.rx.operator.exception;

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
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by wangqinqbin on 2019/1/10.
 */

public class OnExceptionResumeNextOperatorActivity extends BaseActivity {

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

        tv_operator_name.setText("OnExceptionResumeNext");
        tv_operator_effect.setText("遇到错误时，发送1个新的Observable");

//        注：
//
//        onExceptionResumeNext（）拦截的错误 = Exception；若需拦截Throwable请用onErrorResumeNext（）
//        若onExceptionResumeNext（）拦截的错误 = Throwable，则会将错误传递给观察者的onError方法

    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("异常处理操作符---OnExceptionResumeNext");
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
                emitter.onError(new Exception("出现异常"));
                tv_send.append("Observable send : 1" + "\n");
                tv_send.append("Observable send : 2" + "\n");
                tv_send.append("Observable send : 出现异常" + "\n");
            }
        }).onExceptionResumeNext(new ObservableSource<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> observer) {
                tv_send.append("Observable send :出现异常后重新发送 11" + "\n");
                tv_send.append("Observable send :出现异常后重新发送 22" + "\n");
                tv_send.append("Observable send :出现异常后重新发送 onComplete" + "\n");
                observer.onNext(11);
                observer.onNext(22);
                observer.onComplete();
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
