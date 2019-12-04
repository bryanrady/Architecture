package com.bryanrady.architecture.rx.operator.transfer;

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
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangqinqbin on 2019/1/10.
 */

public class FlatMapOperatorActivity extends BaseActivity {

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

        tv_operator_name.setText("FlatMap");
        tv_operator_effect.setText("FlatMap操作符将Observable发射的数据变换为Observables集合，然后将这些Observable" +
                "发射的数据平坦化的放进一个单独的Observable，内部采用merge合并。");
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("转换型操作符---FlatMap");
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

    private ObservableSource<?> getObservableSource(final Integer integer){
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                emitter.onNext(integer.intValue() * 10);
                tv_send.append("Observable send : " + integer.intValue() * 10 +"\n");
                emitter.onNext(integer.intValue() * 20);
                tv_send.append("Observable send : " + integer.intValue() * 20 +"\n");
                emitter.onComplete();
                tv_send.append("Observable send : onComplete" + "\n");
            }
        });
    }

    private void testOperator(){
//        Observable.just(1,2)
//                .flatMap(new Function<Integer, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(Integer integer) throws Exception {
//                        return getObservableSource(integer);
//                    }
//                }).subscribe(new Observer<Object>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                disposable = d;
//            }
//
//            @Override
//            public void onNext(Object o) {
//                tv_receive.append("Observer receive : onNext " + o.toString() + "\n");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                tv_receive.append("Observer receive : onError " + e.getMessage() + "\n");
//            }
//
//            @Override
//            public void onComplete() {
//                tv_receive.append("Observer receive : onComplete " + "\n");
//            }
//        });

        //使用场景   当app  登录前必须先拿到 app的配置（1.0    9.0）  登录

        Observable.just("getConfig","login")
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(final String s) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Object>() {
                            @Override
                            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                emitter.onNext("登录"+s);
                            }
                        });
                    }
                }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                tv_receive.append("Consumer receive : accept " + o.toString() + "\n");
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
