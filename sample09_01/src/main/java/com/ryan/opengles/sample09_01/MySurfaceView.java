package com.ryan.opengles.sample09_01;
import android.opengl.GLSurfaceView;
import android.opengl.GLES20;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;

import com.ryan.opengles.MatrixState;

class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
    private SceneRenderer mRenderer;//������Ⱦ��    
    
    private float mPreviousY;//�ϴεĴ���λ��Y����
    private float mPreviousX;//�ϴεĴ���λ��X����
	
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2); //����ʹ��OPENGL ES2.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }
	
	//�����¼��ص�����
    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dy = y - mPreviousY;//���㴥�ر�Yλ��
            float dx = x - mPreviousX;//���㴥�ر�Xλ��
            mRenderer.yAngle += dx * TOUCH_SCALE_FACTOR;//������y����ת�Ƕ�
            mRenderer.xAngle+= dy * TOUCH_SCALE_FACTOR;//������x����ת�Ƕ�
            requestRender();//�ػ滭��
        }
        mPreviousY = y;//��¼���ر�λ��
        mPreviousX = x;//��¼���ر�λ��
        return true;
    }

	private class SceneRenderer implements Renderer
    {  
		float yAngle;//��Y����ת�ĽǶ�
    	float xAngle; //��X����ת�ĽǶ�
    	//��ָ����obj�ļ��м��ض���
		LoadedObjectVertexOnly lovo;
    	
        public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            //����ϵ��Զ
            MatrixState.pushMatrix();
            MatrixState.translate(0, -2f, -25f);   //ch.obj
            //��Y�ᡢZ����ת
            MatrixState.rotate(yAngle, 0, 1, 0);
            MatrixState.rotate(xAngle, 1, 0, 0);
            
            //�����ص����岻Ϊ�����������
            if(lovo!=null)
            {
            	lovo.drawSelf();
            }   
            MatrixState.popMatrix();                  
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES20.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0,0,0,0f,0f,-1f,0f,1.0f,0.0f);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA
            GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);    
            //����ȼ��
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //�򿪱������   
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
            //����Ҫ���Ƶ�����
            lovo= LoadUtil.loadFromFile("ch.obj", MySurfaceView.this.getResources(),getContext());
        }
    }
}
