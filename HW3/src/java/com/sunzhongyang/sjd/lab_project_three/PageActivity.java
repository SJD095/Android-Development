package com.sunzhongyang.sjd.lab_project_three;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
13331233    孙中阳
2016.10.12  szy@sunzhongyang.com
 */

public class PageActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //不在应用中显示应用名
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //载入布局main_layout
        setContentView(R.layout.page_layout);

        //得到一个WindowManager以获取当前页面的高度
        WindowManager wm = this.getWindowManager();
        int height = wm.getDefaultDisplay().getHeight();

        //构造一个Map以记录联系人姓名机器对应的基本信息
        Map<String, String[]> contacts = new LinkedHashMap<>();

        //联系人基本信息
        String[] Aaron = new String[] {"17715523654", "手机", "江苏苏州电信", "BB4C3B"};
        String[] Elvis = new String[] {"18825653224", "手机", "广东揭阳移动", "c48d30"};
        String[] David = new String[] {"15052116654", "手机", "江苏无锡移动", "4469b0"};
        String[] Edwin = new String[] {"18854211875", "手机", "山东青岛移动", "20A17B"};
        String[] Frank = new String[] {"13955188541", "手机", "安徽合肥移动", "BB4C3B"};
        String[] Joshua = new String[] {"13621574410", "手机", "江苏苏州移动", "c48d30"};
        String[] Ivan = new String[] {"15684122771", "手机", "山东烟台联通", "4469b0"};
        String[] Mark = new String[] {"17765213579", "手机", "广东珠海电信", "20A17B"};
        String[] Joseph = new String[] {"13315466578", "手机", "河北石家庄电信", "BB4C3B"};
        String[] Phoebe = new String[] {"17895466428", "手机", "山东东营移动", "c48d30"};

        //构造Map，key为联系人姓名，value为基本信息数组
        contacts.put("Aaron", Aaron);
        contacts.put("Elvis", Elvis);
        contacts.put("David", David);
        contacts.put("Edwin", Edwin);
        contacts.put("Frank", Frank);
        contacts.put("Joshua", Joshua);
        contacts.put("Ivan", Ivan);
        contacts.put("Mark", Mark);
        contacts.put("Joseph", Joseph);
        contacts.put("Phoebe", Phoebe);

        //获取自上一页面传来的附加信息
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("key");

        //根据id找到四个TextView
        TextView nameInLayout = (TextView) findViewById(R.id.name);
        TextView phone = (TextView) findViewById(R.id.phoneNumber);
        TextView statu = (TextView) findViewById(R.id.status);
        TextView pos = (TextView) findViewById(R.id.position);

        //根据选中的联系人初始化各TextView的内容
        nameInLayout.setText(name);
        phone.setText(contacts.get(name)[0]);
        statu.setText(contacts.get(name)[1]);
        pos.setText(contacts.get(name)[2]);

        //根据id获取返回键和收藏键
        ImageButton back = (ImageButton) findViewById(R.id.backButton);
        final ImageButton star = (ImageButton) findViewById(R.id.star);

        //根据id获取最上方带有背景色的相对布局
        RelativeLayout rel = (RelativeLayout) findViewById(R.id.relativeLayout);

        //根据联系人信息设置相对布局的颜色
        rel.setBackgroundColor(Color.parseColor("#" + contacts.get(name)[3].toUpperCase()));

        //将相对布局的高度设置为当前窗口高度的三分之一
        rel.getLayoutParams().height = height / 3;

        //获取显示四个选项的ListView
        ListView checkList = (ListView) findViewById(R.id.checkListView);

        //初始化ListView
        List<Map<String, String>> data = new ArrayList<>();
        final String[] options = new String[] {"编辑联系人", "分享联系人", "加入黑名单", "删除联系人"};

        //逐项加入
        for(int i = 0; i < options.length; i++)
        {
            Map<String, String> tmp = new LinkedHashMap<>();
            tmp.put("option", options[i]);
            data.add(tmp);
        }

        //构造Adapter
        SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.options_layout, new String[] {"option"}, new int[] {R.id.op});
        checkList.setAdapter(sAdapter);

        //为返回键设置短按事件监听器
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //结束当前页面
                finish();
            }
        });

        //为收藏键设置短按事件监听器
        star.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(star.getTag().equals("empty"))
                {
                    //如果之前是非收藏状态,则转为收藏状态
                    star.setBackgroundResource(R.drawable.full_star);
                    star.setTag("full");
                }
                else
                {
                    star.setBackgroundResource(R.drawable.empty_star);
                    star.setTag("empty");
                }
            }
        });
    }
}
