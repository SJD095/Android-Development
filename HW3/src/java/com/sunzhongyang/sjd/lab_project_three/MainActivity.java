package com.sunzhongyang.sjd.lab_project_three;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
13331233    孙中阳
2016.10.12  szy@sunzhongyang.com
 */

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //不在应用中显示应用名
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //载入布局main_layout
        setContentView(R.layout.main_layout);

        //根据id获取ListView
        ListView contactList = (ListView) findViewById(R.id.listView);

        //构造一个List用于存储姓名首字母和姓名对
        final List<Map<String, Object>> data = new ArrayList<>();
        //构造一个字符串数组用于存储所有姓名
        final String[] contactNames = new String[] {"Aaron","Elvis","David","Edwin","Frank","Joshua","Ivan","Mark","Joseph","Phoebe"};

        //将姓名首字母和姓名的组合存入List
        for(int i = 0; i < contactNames.length; i++)
        {
            Map<String, Object> tmp = new LinkedHashMap<>();
            tmp.put("firstC", contactNames[i].charAt(0));
            tmp.put("contactN", contactNames[i]);
            data.add(tmp);
        }

        //构造一个SimpleAdapter,然后使用此Adapter初始化ListView
        final SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.item_layout, new String[] {"firstC", "contactN"}, new int[] {R.id.firstChar, R.id.contactName});
        contactList.setAdapter(sAdapter);

        //为ListView的每一个Item注册短按事件监听器
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int i, long l)
            {
                //构造一个Intent,携带被点击Item的姓名的信息创建一个联系人详情页
                Intent intent = new Intent(MainActivity.this, PageActivity.class);
                intent.putExtra("key", (String) data.get(i).get("contactN"));
                startActivity(intent);
            }
        });

        ////为ListView的每一个Item注册长按事件监听器
        contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int i, long l)
            {
                //用count替换 i 以避免与其他使用 i 的函数混淆
                final int count = i;

                //弹出一个提示框
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("删除联系人")
                        .setMessage("确定删除联系人" + data.get(count).get("contactN") + "?")
                        //点击确定按钮则弹框提示确定按钮被按下
                        .setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j)
                            {
                                //从数据源删除有关被选中联系人的数据,并通知Adapter有关更改
                                data.remove(count);
                                sAdapter.notifyDataSetChanged();
                            }
                        })
                        //点击取消按钮则提示取消按钮被按下
                        .setNegativeButton("取消", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j)
                            {

                            }
                        })
                        .create()
                        .show();

                return true;
            }
        });
    }
}
