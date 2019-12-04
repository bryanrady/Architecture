package com.bryanrady.architecture.ioc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Administrator on 2019/6/24.
 */

public class IocBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.inject(this);
    }
}
