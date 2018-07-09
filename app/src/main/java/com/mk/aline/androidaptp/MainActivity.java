package com.mk.aline.androidaptp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mk.aline.apt.OnceClick;
import com.mk.aline.aptlib.AptLibInit;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //类似于ButterKnife的Bind方法。初始化OnceClick
        AptLibInit.androidApt(this);
        text = (TextView) findViewById(R.id.textView);
    }

    @OnceClick(R.id.btn)
    public void once() {
        //点击事件
        Log.d("tag", "onceMe:" + System.currentTimeMillis());
    }

    @OnceClick(R.id.btn2)
    public void onceMe(View v) {
        ((Button) v).setText("click" + num++);
        Log.d("tag", "onceMe");
    }
}
