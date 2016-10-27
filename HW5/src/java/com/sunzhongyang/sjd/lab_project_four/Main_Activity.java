package com.sunzhongyang.sjd.lab_project_four;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/*
13331233    孙中阳
2016.10.21  szy@sunzhongyang.com
 */

public class Main_Activity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //不在应用中显示应用名
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //载入布局main_layout
        setContentView(R.layout.main_activity);

        //获取动态注册按钮和静态注册按钮
        Button dynamicButton = (Button) findViewById(R.id.dynamic_register_button);
        Button staticButton = (Button) findViewById(R.id.static_register_button);

        //为动态注册按钮绑定点击事件
        dynamicButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //跳转到动态注册页面
                Intent intentDynamic = new Intent(Main_Activity.this, Dynamic_Register.class);
                startActivity(intentDynamic);
            }
        });

        //为静态注册按钮绑定点击事件
        staticButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //跳转到静态注册页面
                Intent staticIntent = new Intent(Main_Activity.this, Static_Register.class);
                startActivity(staticIntent);
            }
        });
    }
}