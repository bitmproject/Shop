package com.example.aunshon.shop;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class login extends AppCompatActivity {
    RelativeLayout r1,r2;

    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        r1=findViewById(R.id.rela_1);
        r2=findViewById(R.id.rela_2);

        handler.postDelayed(runnable,400);
    }
}
