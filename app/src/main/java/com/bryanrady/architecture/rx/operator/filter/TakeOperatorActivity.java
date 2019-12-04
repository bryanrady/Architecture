package com.bryanrady.architecture.rx.operator.filter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by wangqinqbin on 2019/1/10.
 */

public class TakeOperatorActivity extends BaseActivity {

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

        tv_operator_name.setText("Take");
        tv_operator_effect.setText("只发射开始的N项数据或者一定时间内的数据。内部通过OperatorTake和OperatorTakeTimed过滤数据。");
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("过滤型操作符---Take");
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
        //只发射这段数据中的前3个数据
        tv_send.append("Observable send : 1" + "\n");
        tv_send.append("Observable send : 2" + "\n");
        tv_send.append("Observable send : 3" + "\n");
        tv_send.append("Observable send : 4" + "\n");
        tv_send.append("Observable send : 5" + "\n");

        Observable.just(1,2,3,4,5)
                .take(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        tv_receive.append("Consumer receive : accept " + integer.intValue() + "\n");
                    }
                });

        //只发送前100 ms 的数据
        tv_send.append("Observable send : 1" + "\n");
        tv_send.append("Observable send : 2" + "\n");
        tv_send.append("Observable send : 3" + "\n");
        tv_send.append("Observable send : 4" + "\n");
        tv_send.append("Observable send : 5" + "\n");
        tv_send.append("Observable send : 6" + "\n");
        tv_send.append("Observable send : 7" + "\n");
        tv_send.append("Observable send : 8" + "\n");

        Observable.just(1,2,3,4,5,6,7,8)
                .take(100, TimeUnit.MICROSECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        tv_receive.append("Consumer receive : accept " + integer.intValue() + "\n");
                    }
                });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(11);
                tv_send.append("Observable send : 11" + "\n");

                emitter.onNext(22);
                tv_send.append("Observable send : 22" + "\n");

                emitter.onNext(33);
                tv_send.append("Observable send : 33" + "\n");

                emitter.onComplete();
                tv_send.append("Observable send : onComplete" + "\n");
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
