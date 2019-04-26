package com.ryan.opengles.sample05_01;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ryan.opengles.MatrixState;
import com.ryan.opengles.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class SixPointedStar {

    private int mProgram; //自定义渲染管线程序id
    private int muMVPMatrixHandle;//总变换矩阵引用(在顶点着色器中)
    private int maPositionHandle; //顶点位置属性引用(在顶点着色器中)
    private int maColorHandle; //顶点颜色属性引用(在顶点着色器中)

    private String mVertexShader;//顶点着色器代码脚本
    private String mFragmentShader;//片元着色器代码脚本

    private static float[] mMMatrix = new float[16]; // 变换矩阵


    private FloatBuffer mVertexBuffer; // 顶点坐标缓冲区
    private FloatBuffer mColorBuffer; // 顶点颜色缓冲区

    int vCount = 0; // 顶点个数

    public float xAngle = 0; 
    public float yAngle = 0;
    private float UNIT_SIZE = 1;

    public SixPointedStar(MySurfaceView mv, float R, float r, float z) {
        // 初始化顶点数据
        initVertexData(R, r, z);
        // 初始化着色器
        initShader(mv);
    }

    // 这里只是一个六角星
    private void initVertexData(float R, float r, float z) {
        // 存放所有顶点的list
        List<Float> flist = new ArrayList<>();
        float tempAngle = 360 / 6;
        for(float angle=0; angle<360; angle+=tempAngle) {
            // 六角星的一个角是由2个三角形合并而成，所以是3+3=6个顶点
            // 一共有12个三角形，6*6=36个顶点

            // 中心点
            flist.add(0f);
            flist.add(0f);
            flist.add(z);

            flist.add((float) (R*UNIT_SIZE*Math.cos(Math.toRadians(angle))));
            flist.add((float) (R*UNIT_SIZE*Math.sin(Math.toRadians(angle))));
            flist.add(z);

            flist.add((float) (r*UNIT_SIZE*Math.cos(Math.toRadians(angle+tempAngle/2))));
            flist.add((float) (r*UNIT_SIZE*Math.sin(Math.toRadians(angle+tempAngle/2))));
            flist.add(z);

            flist.add(0f);
            flist.add(0f);
            flist.add(z);

            flist.add((float) (r*UNIT_SIZE*Math.cos(Math.toRadians(angle+tempAngle/2))));
            flist.add((float) (r*UNIT_SIZE*Math.sin(Math.toRadians(angle+tempAngle/2))));
            flist.add(z);

            flist.add((float) (R*UNIT_SIZE*Math.cos(Math.toRadians(angle+tempAngle))));
            flist.add((float) (R*UNIT_SIZE*Math.sin(Math.toRadians(angle+tempAngle))));
            flist.add(z);
        }

        vCount = flist.size()/3; // 6*6*3/3 一共36个顶点
        float[] vertexArray = new float[flist.size()];
        for (int i = 0; i< vCount; i++) {
            vertexArray[i*3]=flist.get(i*3);
            vertexArray[i*3+1]=flist.get(i*3+1);
            vertexArray[i*3+2]=flist.get(i*3+2);
        }
        // float数组转换成byte数组
        //ByteBuffer是其它类型的基础，因为分配内存是通过ByteBuffer的allocate来进行的，然后通过 ByteBuffer 的asXXXBuffer()转为其它类型的buffer：
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length*4); // 一共有 36*3=108个float数组，每个float占用4个字节
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer(); // 在转换成float数组？？？
        mVertexBuffer.put(vertexArray);
        mVertexBuffer.position(0);

        // 设置顶点颜色
        float[] colorArray = new float[vCount*4]; // 一共12个顶点
        for(int i=0;i<vCount;i++)
        {
            if(i%3==0){ // 如果是圆心的点
                colorArray[i*4]=1;
                colorArray[i*4+1]=1;
                colorArray[i*4+2]=1;
                colorArray[i*4+3]=0;
            }
            else{
                colorArray[i*4]=0.45f;
                colorArray[i*4+1]=0.75f;
                colorArray[i*4+2]=0.75f;
                colorArray[i*4+3]=0;
            }
        }
        ByteBuffer cbb = ByteBuffer.allocateDirect(colorArray.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colorArray);
        mColorBuffer.position(0);
    }

    private void initShader(MySurfaceView glSurfaceView) {
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.glsl", glSurfaceView.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.glsl", glSurfaceView.getResources());

        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }


    public void drawSelf() {
        // 设置程序
        GLES20.glUseProgram(mProgram);

        // 初始化矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 1, 1, 1); // 初始化变化矩阵,为什么这里y=1?? --- 似乎有一个设置成1就可以
        Matrix.translateM(mMMatrix, 0, 0, 0, 1); // 设置沿Z轴正向位移1
        Matrix.rotateM(mMMatrix,0, yAngle,0,1,0); // 设置绕Y轴旋转yAngle度
        Matrix.rotateM(mMMatrix,0, xAngle,1,0,0); // 设置绕X轴旋转xAngle度
        // 传入总的变化矩阵
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(mMMatrix), 0);

        // 传入顶点数据
        GLES20.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3*4,
                        mVertexBuffer
                );
        // 传入颜色数据
        GLES20.glVertexAttribPointer
                (
                        maColorHandle,
                        4,
                        GLES20.GL_FLOAT,
                        false,
                        4*4,
                        mColorBuffer
                );
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maColorHandle);

        // 开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); // vCount 有几个顶点
    }
}
