package com.bryanrady.dexelements;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Created by 48608 on 2018/1/12.
 */

public class DexFirstActivity extends DexBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_hook_login_first);
    }

}
