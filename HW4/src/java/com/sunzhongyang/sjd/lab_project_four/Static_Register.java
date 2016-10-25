package com.sunzhongyang.sjd.lab_project_four;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Static_Register extends Activity
{

    private static final String STATICACTION = "com.sunzhongyang.sjd.lab_project_four.staticcreceiver";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //不在应用中显示应用名
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //载入布局main_layout
        setContentView(R.layout.static_register);

        //创建一个字符串数组用于存储每个list item的文本内容
        final String[] fruits = new String[] {"Apple","Banana","Cherry","Coco","Kiwi","Orange","Pear","Strawberry","Watermelon"};
        //创建一个整形数组用于存储所有list item的图片的id
        final int [] resIds = {R.mipmap.apple, R.mipmap.banana, R.mipmap.cherry, R.mipmap.coco, R.mipmap.kiwi, R.mipmap.orange, R.mipmap.pear, R.mipmap.strawberry, R.mipmap.watermelon};

        //获取ListView
        ListView list = (ListView) findViewById(R.id.list_view);

        //初始化ListView
        list.setAdapter(new ListViewAdapter(fruits, resIds));

        //为ListView设置点击事件
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int i, long l)
            {
                //设置一个bundle用于传送list item的文本和图像id
                Bundle bundle = new Bundle();
                bundle.putString("Content", fruits[i]);
                bundle.putInt("PictureId", resIds[i]);

                //发送广播
                Intent intent = new Intent(STATICACTION);
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
        });
    }

    //一个自定义的ListViewAdapter
    public class ListViewAdapter extends BaseAdapter
    {
        //用于存放所有list item的view
        View[] itemViews;

        public ListViewAdapter(String[] itemTexts, int[] itemImageRes)
        {
            itemViews = new View[itemTexts.length];

            //按次序初始化每个list item
            for (int i = 0; i < itemViews.length; ++i) {
                itemViews[i] = makeItemView(itemTexts[i],
                        itemImageRes[i]);
            }
        }

        private View makeItemView(String strText, int resId)
        {
            LayoutInflater inflater = (LayoutInflater)Static_Register.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.item, null);

            //初始化某个list item的描述信息
            TextView text = (TextView)itemView.findViewById(R.id.message);
            text.setText(strText);
            //初始化某个list item的图像
            ImageView image = (ImageView)itemView.findViewById(R.id.apple_view);
            image.setImageResource(resId);

            return itemView;
        }

        //返回当前ListView中item的数目
        public int getCount() {
            return itemViews.length;
        }

        //获取某个位置的list item
        public View getItem(int position) {
            return itemViews[position];
        }

        //获取某一个位置的list item的id
        public long getItemId(int position) {
            return position;
        }

        //获取某一个位置的list item的view
        public View getView(int position, View convertView, ViewGroup parent)
        {
            return itemViews[position];
        }
    }
}