package com.bryanrady.architecture.ioc;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;


/**
 * Created by david on 2017/8/21.
 */

public class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.inject(this);
    }

}
