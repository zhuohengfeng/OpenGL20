package com.ryan.opengles.sample05_02;

import android.content.Context;
import android.opengl.GLES20;

import com.ryan.opengles.MatrixState;
import com.ryan.opengles.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Cube {

    private int mProgram; //自定义渲染管线着色器程序id
    private int muMVPMatrixHandle; // 总变换矩阵引用
    private int maPositionHandle; // 顶点位置属性引用
    private int maColorHandle; // 顶点颜色属性引用

    private String mVertexShader; // 顶点着色器代码
    private String mFragmentShader; // 片元着色器代码

    private FloatBuffer mVertexBuffer; // 顶点坐标数据缓存
    private FloatBuffer mColorBuffer; // 顶点颜色数据缓存

    private int vCount = 0; // 一共有多少个顶点

    public Cube(Context context) {
        initVetexData();
        initShader(context);
    }

    //初始化顶点坐标与着色数据的方法
    private void initVetexData() {

        // 开始填充顶点，6个面， 每个面4个三角形组成(中心点)，所以一个面需要12个顶点，6个面就需要12*6个顶点
        vCount = 12 * 6;

        // 一个顶点有3个坐标(x, y, z), 所以一共有12*6*3=216个float数据
        float[] vertices = new float[] {
            // 前面
            0,0,1,
            1,1,1,
            -1,1,1,
            0,0,1,
            -1,1,1,
            -1,-1,1,
            0,0,1,
            -1,-1,1,
            1,-1,1,
            0,0,1,
            1,-1,1,
            1,1,1,

            // 后面
            0,0,-1,
            1,1,-1,
            1,-1,-1,
            0,0,-1,
            1,-1,-1,
            -1,-1,-1,
            0,0,-1,
            -1,-1,-1,
            -1,1,-1,
            0,0,-1,
            -1,1,-1,
            1,1,-1,

            // 左面
            -1,0,0,
            -1,1,1,
            -1,1,-1,
            -1,0,0,
            -1,1,-1,
            -1,-1,-1,
            -1,0,0,
            -1,-1,-1,
            -1,-1,1,
            -1,0,0,
            -1,-1,1,
            -1,1,1,

            // 右面
            1,0,0,
            1,1,1,
            1,-1,1,
            1,0,0,
            1,-1,1,
            1,-1,-1,
            1,0,0,
            1,-1,-1,
            1,1,-1,
            1,0,0,
            1,1,-1,
            1,1,1,

            // 上面
            0,1,0,
            1,1,1,
            1,1,-1,
            0,1,0,
            1,1,-1,
            -1,1,-1,
            0,1,0,
            -1,1,-1,
            -1,1,1,
            0,1,0,
            -1,1,1,
            1,1,1,

            // 下面
            0,-1,0,
            1,-1,1,
            -1,-1,1,
            0,-1,0,
            -1,-1,1,
            -1,-1,-1,
            0,-1,0,
            -1,-1,-1,
            1,-1,-1,
            0,-1,0,
            1,-1,-1,
            1,-1,1,
        };
        //由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4); // 每个float类型占4个自己
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        //顶点颜色值数组，每个顶点4个色彩值RGBA
        float colors[]=new float[]{
                //前面
                1,1,1,0,//中间为白色
                1,0,0,0,
                1,0,0,0,
                1,1,1,0,//中间为白色
                1,0,0,0,
                1,0,0,0,
                1,1,1,0,//中间为白色
                1,0,0,0,
                1,0,0,0,
                1,1,1,0,//中间为白色
                1,0,0,0,
                1,0,0,0,
                //后面
                1,1,1,0,//中间为白色
                0,0,1,0,
                0,0,1,0,
                1,1,1,0,//中间为白色
                0,0,1,0,
                0,0,1,0,
                1,1,1,0,//中间为白色
                0,0,1,0,
                0,0,1,0,
                1,1,1,0,//中间为白色
                0,0,1,0,
                0,0,1,0,
                //左面
                1,1,1,0,//中间为白色
                1,0,1,0,
                1,0,1,0,
                1,1,1,0,//中间为白色
                1,0,1,0,
                1,0,1,0,
                1,1,1,0,//中间为白色
                1,0,1,0,
                1,0,1,0,
                1,1,1,0,//中间为白色
                1,0,1,0,
                1,0,1,0,
                //右面
                1,1,1,0,//中间为白色
                1,1,0,0,
                1,1,0,0,
                1,1,1,0,//中间为白色
                1,1,0,0,
                1,1,0,0,
                1,1,1,0,//中间为白色
                1,1,0,0,
                1,1,0,0,
                1,1,1,0,//中间为白色
                1,1,0,0,
                1,1,0,0,
                //上面
                1,1,1,0,//中间为白色
                0,1,0,0,
                0,1,0,0,
                1,1,1,0,//中间为白色
                0,1,0,0,
                0,1,0,0,
                1,1,1,0,//中间为白色
                0,1,0,0,
                0,1,0,0,
                1,1,1,0,//中间为白色
                0,1,0,0,
                0,1,0,0,
                //下面
                1,1,1,0,//中间为白色
                0,1,1,0,
                0,1,1,0,
                1,1,1,0,//中间为白色
                0,1,1,0,
                0,1,1,0,
                1,1,1,0,//中间为白色
                0,1,1,0,
                0,1,1,0,
                1,1,1,0,//中间为白色
                0,1,1,0,
                0,1,1,0,
        };
        //创建顶点着色数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mColorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mColorBuffer.put(colors);//向缓冲区中放入顶点着色数据
        mColorBuffer.position(0);//设置缓冲区起始位置
    }

    private void initShader(Context context) {
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.glsl", context.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.glsl", context.getResources());

        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf() {
        // 使用程序
        GLES20.glUseProgram(mProgram);
        // 将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);

        //为画笔指定顶点位置数据
        GLES20.glVertexAttribPointer(
                maPositionHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                3*4,
                mVertexBuffer
        );

        //为画笔指定顶点着色数据
        GLES20.glVertexAttribPointer(
                maColorHandle,
                4,
                GLES20.GL_FLOAT,
                false,
                4*4,
                mColorBuffer
        );

        //允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maColorHandle);

        //绘制立方体
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0, vCount);
    }

}
