package com.ryan.opengles.sample05_01;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * 不同投影模式切换，在onSurfaceChanged设置
 */
public class Sample05_01 extends Activity {

    private MySurfaceView mySurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置为横屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mySurfaceView = new MySurfaceView(this);
        setContentView(mySurfaceView);

        mySurfaceView.requestFocus();
        mySurfaceView.setFocusableInTouchMode(true);
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
