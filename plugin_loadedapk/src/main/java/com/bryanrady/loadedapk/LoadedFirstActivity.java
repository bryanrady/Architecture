package com.bryanrady.loadedapk;
import android.os.Bundle;
import android.support.annotation.Nullable;


/**
 * Created by 48608 on 2018/1/12.
 */

public class LoadedFirstActivity extends LoadedBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_hook_login_first);
    }

}