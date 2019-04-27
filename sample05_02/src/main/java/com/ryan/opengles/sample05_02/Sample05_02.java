package com.ryan.opengles.sample05_02;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Sample05_02 extends Activity {

    private MySurfaceView mySurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置为横屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        mySurfaceView = new MySurfaceView(this);
        this.setContentView(mySurfaceView);
        mySurfaceView.requestFocus();// 获取焦点
        mySurfaceView.setFocusableInTouchMode(true);// 设置为可触控
    }

    @Override
    protected void onResume() {
        super.onResume();
        mySurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySurfaceView.onPause();
    }
}
