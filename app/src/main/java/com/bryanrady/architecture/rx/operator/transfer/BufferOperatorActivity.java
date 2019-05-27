package com.bryanrady.architecture.rx.operator.transfer;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bryanrady.architecture.BaseActivity;
import com.bryanrady.architecture.R;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by wangqinqbin on 2019/1/10.
 */

public class BufferOperatorActivity extends BaseActivity {

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

        tv_operator_name.setText("buffer");
        tv_operator_effect.setText("buffer操作符是把多个元素打包成一个元素然后一次性发送数据。");
    }

    private void initToolbar() {
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("转换型操作符---buffer");
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

//    适用场景   10000条数据插入到数据库中时  每一条数据产生都需要时间
//          如果产生一条 插入一条比较浪给时间，全部一次性插入用户等的太久
//          采取buffer的形式  将10000条  分成 一小段执行

    private void testOperator(){
        tv_send.append("Observable send : 1" + "\n");
        tv_send.append("Observable send : 2" + "\n");
        tv_send.append("Observable send : 3" + "\n");
        tv_send.append("Observable send : 4" + "\n");
        tv_send.append("Observable send : 5" + "\n");
        tv_send.append("Observable send : 6" + "\n");
        Observable.just(1,2,3,4,5,6)
                .buffer(4)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        for(Integer integer : integers){
                            tv_receive.append("accept receive : accept " + integer + "\n");
                        }
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
