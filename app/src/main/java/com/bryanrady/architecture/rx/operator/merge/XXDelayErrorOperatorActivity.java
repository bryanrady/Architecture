package com.bryanrady.architecture.rx.operator.merge;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import java.util.Arrays;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by wangqinqbin on 2019/1/10.
 */

public class XXDelayErrorOperatorActivity extends BaseActivity {

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

        tv_operator_name.setText("XXDelayError");
        tv_operator_effect.setText("按顺序连接多个Observables。需要注意的是Observable.concat(a,b)等价于a.concatWith(b)。concat  最多连接四个");

        /**
         * 我们知道, 在进行一些合并操作时,如果碰到某个Observable发送了Error事件,则操作就会终止.
         * 这时候如果需要先暂时忽略错误,将相应的操作进行完后再将发送Error事件,测可以用该方法
         * 对应的DelayError版本的方法.
         *
         * 很多函数都有提供DelayError版本的方法, 比如:
         *  combineLatestDelayError,concatDelayError, mergeDelayError, concatMapDelayError,
         *  switchMapDelayError, switchOnNextDelayError.
         */


    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("合并操作符---XXDelayError");
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

        /**
         * 举个例子, Observable.concat是将几个Observable的数据合并, 如下所示,
         * 第一个Observable除了发射数据外,还会发射一个Error,如果使用concat,
         * 则无法合并第二个Observable的内容, 具体如下:
         *
         */

        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("a1");
                emitter.onNext("a2");
                emitter.onNext("a3");
                emitter.onError(new Throwable("error from obs1"));

                tv_send.append("Observable send : a1" + "\n");
                tv_send.append("Observable send : a2" + "\n");
                tv_send.append("Observable send : a3" + "\n");
                tv_send.append("Observable send : onError : error from obs1 " + "\n");
            }
        });
        Observable<String> observable2 = Observable.just("b1","b2","b3");
        tv_send.append("Observable send : b1" + "\n");
        tv_send.append("Observable send : b2" + "\n");
        tv_send.append("Observable send : b3" + "\n");

        Observable.concat(observable1,observable2)
                .subscribe(new Observer<String>() {
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
                        tv_receive.append("Observable receive : onError " + e.getMessage() + "\n");
                    }

                    @Override
                    public void onComplete() {
                        tv_receive.append("Observer receive : onComplete " + "\n");
                    }
                });


        Observable.concatDelayError(Arrays.asList(observable1,observable2))
                .subscribe(new Observer<String>() {
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
