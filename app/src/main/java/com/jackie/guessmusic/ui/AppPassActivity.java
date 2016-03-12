package com.jackie.guessmusic.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.jackie.guessmusic.R;

/**
 * Created by Jackie on 2016/3/12.
 * 通关界面
 */
public class AppPassActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_pass);

        //隐藏右上角金币图标
        FrameLayout coinLayout = (FrameLayout) findViewById(R.id.top_bar_coin_layout);
        coinLayout.setVisibility(View.GONE);
    }
}
