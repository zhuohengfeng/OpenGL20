package com.ryan.opengles.sample05_01;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.ryan.opengles.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MySurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 360;
    private float mPreviousX;
    private float mPreviousY;

    private SceneRenderer mSceneRenderer;

    public MySurfaceView(Context context) {
        super(context);
        // 设置opengl版本
        this.setEGLContextClientVersion(2);
        // 设置渲染器
        mSceneRenderer = new SceneRenderer();
        this.setRenderer(mSceneRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取此次按下的x, y坐标
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = x - mPreviousX;
            float dy = y = mPreviousY;
            for (SixPointedStar star : mSceneRenderer.mSixStars) {
                star.xAngle += dx * TOUCH_SCALE_FACTOR;
                star.yAngle += dy * TOUCH_SCALE_FACTOR;
            }
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {

        private SixPointedStar[] mSixStars = new SixPointedStar[6];

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // 设置背景
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
            // 创建六角星
            for (int i = 0; i < mSixStars.length; i++) {
                // 创建六角星, x坐标， y坐标, z坐标(每个z上有偏移)
                mSixStars[i] = new SixPointedStar(MySurfaceView.this, 0.2f, 0.5f, -0.8f*i);
            }
            // 打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // 设置视口
            GLES20.glViewport(0, 0, width, height);
            // 计算宽高比
            float ratio = (float) width / height;
            // 设置正交矩阵
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 10);
            // 设置摄像机位置
            MatrixState.setCamera(
                    0, 0, 3f,  // 摄像机位置
                    0, 0, 0,    // 摄像机朝向原点(0, 0)
                    0, 1.0f, 0  // 摄像机朝上(y+)
            );
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // 每次绘制之前都清一下缓存，颜色缓存和深度缓存
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            for (SixPointedStar star : mSixStars) {
                star.drawSelf();
            }
        }
    }

}
