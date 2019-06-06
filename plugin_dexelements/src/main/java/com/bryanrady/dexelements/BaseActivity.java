package com.bryanrady.dexelements;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * Created by Administrator on 2019/6/5.
 */

public class BaseActivity extends Activity {

    @Override
    public AssetManager getAssets() {
        if(getApplication() != null && getApplication().getAssets() != null){
            return getApplication().getAssets();
        }
        return super.getAssets();
    }

    @Override
    public Resources getResources() {
        if(getApplication() != null && getApplication().getResources() != null){
            return getApplication().getResources();
        }
        return super.getResources();
    }

//    @Override
//    public Resources.Theme getTheme() {
//        if(getApplication() != null && getApplication().getTheme() != null){
//            return getApplication().getTheme();
//        }
//        return super.getTheme();
//    }
}
