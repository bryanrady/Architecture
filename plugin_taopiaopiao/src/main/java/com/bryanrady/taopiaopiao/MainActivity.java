package com.bryanrady.taopiaopiao;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name = savedInstanceState.getString("name");
        int age = savedInstanceState.getInt("age");
        Toast.makeText(mThat,"我是+"+name+",今年+"+age+"了！",Toast.LENGTH_SHORT).show();

        findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mThat,SecondActivity.class));
            }
        });

        findViewById(R.id.startService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(mThat, OneService.class));
            }
        });

        findViewById(R.id.sendDynamicBroad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //动态注册广播 并发送
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(DynamicBroadcastReceiver.DYNAMIC_ACTION);
                registerReceiver(new DynamicBroadcastReceiver(), intentFilter);

                Intent intent = new Intent();
                intent.setAction(DynamicBroadcastReceiver.DYNAMIC_ACTION);
                sendBroadcast(intent);
            }
        });
    }
}
