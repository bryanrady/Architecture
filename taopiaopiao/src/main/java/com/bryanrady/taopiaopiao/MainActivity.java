package com.bryanrady.taopiaopiao;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends TppBaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name = savedInstanceState.getString("name");
        int age = savedInstanceState.getInt("age");
        Toast.makeText(mThat,"我是+"+name+",今年+"+age+"了！",Toast.LENGTH_LONG).show();

        findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mThat,SecondActivity.class));
                startService(new Intent(mThat, OneService.class));
            }
        });

    }
}
