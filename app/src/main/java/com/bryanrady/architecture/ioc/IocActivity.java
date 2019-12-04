package com.bryanrady.architecture.ioc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanrady.architecture.R;
import com.bryanrady.architecture.ioc.annotation.ContentView;
import com.bryanrady.architecture.ioc.annotation.OnClick;
import com.bryanrady.architecture.ioc.annotation.OnLongClick;
import com.bryanrady.architecture.ioc.annotation.ViewInject;

import androidx.appcompat.widget.Toolbar;


/**
 * Created by Administrator on 2019/6/24.
 */

@ContentView(R.layout.activity_ioc_use)
public class IocActivity extends IocBaseActivity {

    @ViewInject(R.id.btn_ioc_use)
    Button btn_ioc_use;

    @ViewInject(R.id.btn_ioc_use_dialog)
    Button btn_ioc_use_dialog;

    @ViewInject(R.id.toolbar)
    Toolbar mToolbar;

    @ViewInject(R.id.ll_toolbar_back)
    LinearLayout llBack;

    @ViewInject(R.id.tv_toolbar_center_title)
    TextView tvToolbarTitle;

    @ViewInject(R.id.tv_toolbar_right_title)
    TextView tvRightTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        llBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText("Xutils IOC 框架");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        btn_ioc_use.setText("IOC 事件注入");
    }

    @OnClick({R.id.btn_ioc_use,R.id.btn_ioc_use_dialog})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_ioc_use:
                Toast.makeText(this,"点击事件",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_ioc_use_dialog:
                NewsDialog newsDialog = new NewsDialog(this);
                newsDialog.show();
                break;
        }
    }

    @OnLongClick({R.id.btn_ioc_use})
    public boolean onLongClick(View view){
        switch (view.getId()){
            case R.id.btn_ioc_use:
                Toast.makeText(this,"长按事件",Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }


}
