package com.ryan.opengles;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryan.opengles.sample03_1.Sample03_1;
import com.ryan.opengles.sample05_01.Sample05_01;
import com.ryan.opengles.sample05_02.Sample05_02;
import com.ryan.opengles.sample09_01.Sample09_01;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity{

    private List<ItemBean> mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = getData();
        MyAdapter adapter = new MyAdapter(this);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, Sample03_1.class);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, Sample05_01.class);
                        break;

                    case 2:
                        intent = new Intent(MainActivity.this, Sample05_02.class);
                        break;

                    case 3:
                        break;

                    case 4:
                        break;

                    case 5:
                        intent = new Intent(MainActivity.this, Sample09_01.class);
                        break;

                    case 6:
                        break;
                }


                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
    }

    private List<ItemBean> getData() {
        List<ItemBean> list = new ArrayList<>();

        try {
            // ---------------第3章 概览--------------
            ItemBean sample3_1 = new ItemBean();
            sample3_1.name = "第3章 概览";
            sample3_1.info = "绘制一个转动的三角形";
            list.add(sample3_1);

            // ---------------第5章 投影及各种变化--------------
            ItemBean sample5_1 = new ItemBean();
            sample5_1.name = "第5章 正交投影和透视投影";
            sample5_1.info = "绘制正交&透视投影的六角形，投影方式不同，显示大小也不同";
            list.add(sample5_1);

            ItemBean sample5_2 = new ItemBean();
            sample5_2.name = "第5章 各种变化";
            sample5_2.info = "绘制一个立方体，实现平移，旋转，缩放";
            list.add(sample5_2);

            ItemBean sample5_3 = new ItemBean();
            sample5_3.name = "第5章 绘制方式";
            sample5_3.info = "各种绘制方式，点，线，三角形条带，扇面";
            list.add(sample5_3);




            // ---------------第6章 光照--------------
            ItemBean sample6_1 = new ItemBean();
            sample6_1.name = "第6章 光照";
            sample6_1.info = "绘制一个球";
            list.add(sample6_1);

            // ---------------第7章 纹理映射--------------


            // ---------------第8章 3D基本形状--------------


            // ---------------第9章 3D模型加载--------------
            ItemBean sample9_1 = new ItemBean();
            sample9_1.name = "第9章 3D模型加载";
            sample9_1.info = "加载一个水壶";
            list.add(sample9_1);

            // ---------------第10章 混合与雾--------------


            // ---------------第11章 混合与雾--------------


            // ---------------第12章 几种剪裁与测试--------------

            // ---------------第13章 顶点着色器的妙用--------------

            // ---------------第14章 片元着色器的妙用--------------

            // ---------------第15章 真实光学环境的模拟--------------

            return list;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return list;
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder=new ViewHolder();
                convertView = mInflater.inflate(R.layout.activity_main, null);
                holder.name = convertView.findViewById(R.id.name);
                holder.info = convertView.findViewById(R.id.info);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.name.setText(mData.get(position).name);
            holder.info.setText(mData.get(position).info);

            return convertView;
        }
    }

    public final class ViewHolder{
        public TextView name;
        public TextView info;
    }


}