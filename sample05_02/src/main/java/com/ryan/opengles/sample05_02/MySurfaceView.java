package com.ryan.opengles.sample05_02;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.ryan.opengles.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MySurfaceView extends GLSurfaceView {

    private Context mContext;

    public SceneRenderer mSceneRenderer;

    public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);
        mContext = context;
        mSceneRenderer = new SceneRenderer();
        this.setRenderer(mSceneRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }


    private class SceneRenderer implements GLSurfaceView.Renderer {

        Cube mCube;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.5f,0.5f,0.5f, 1.0f);
            mCube = new Cube(mContext);
            // 打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            // 打开背面裁剪
            GLES20.glEnable(GLES20.GL_CULL_FACE);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float)width/height;
            MatrixState.setProjectFrustum(-ratio*0.8f, ratio*1.2f, -1, 1, 20, 100);
            MatrixState.setCamera(-16f, 8f, 45, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            MatrixState.setInitStack();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            // 绘制第一个
            MatrixState.pushMatrix();
            mCube.drawSelf();
            MatrixState.popMatrix();
            // 绘制第二个
            MatrixState.pushMatrix();
            MatrixState.translate(4, 0, 0);
            mCube.drawSelf();
            MatrixState.popMatrix();
        }
    }

}
