package com.bryanrady.architecture.databind;

import android.os.Bundle;
import android.os.Handler;

import com.bryanrady.architecture.R;
import com.bryanrady.architecture.databinding.ActivityDatabindingUseBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class DataBindingUseActivity extends AppCompatActivity {

    private User mUser;
    private Animal mAnimal;
    private ActivityDatabindingUseBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_databinding_use);
        mBinding = (ActivityDatabindingUseBinding) viewDataBinding;
        initData();
    }

    private void initData() {
        mUser = new User();
        mUser.setName("wangqingbin");
        mUser.setSex("男");
        mUser.setAge(26);
        mUser.setHeaderPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1590666180039&di=6d8c5c16e4b1d5907eca1759431da802&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201603%2F14%2F20160314234415_8itHx.thumb.700_0.jpeg");

        mAnimal = new Animal();
        mAnimal.setName("大熊猫");
        mAnimal.setWeight(200f);

        mBinding.setUser(mUser);
        mBinding.setAnimal(mAnimal);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mUser.setName("bryanrady");
                mUser.setAge(24);

                mAnimal.setName("吃胖了的大熊猫");
                mAnimal.setWeight(240f);
            }
        },3000);
    }



}
