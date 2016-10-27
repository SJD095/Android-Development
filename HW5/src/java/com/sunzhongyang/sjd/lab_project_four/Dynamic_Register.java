package com.sunzhongyang.sjd.lab_project_four;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class Dynamic_Register extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //不在应用中显示应用名
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //载入布局main_layout
        setContentView(R.layout.dynamic_register);

        //一个过滤器
        final String DYNAMICACTION = "com.sunzhongyang.sjd.lab_project_four.dynamicreceiver";

        //获取注册或取消注册的按钮以及发送按钮
        final Button dynamicButton = (Button) findViewById(R.id.dynamic_button);
        final Button sendButton = (Button) findViewById(R.id.send_button);

        //获取用于输入通知信息的输入框
        final EditText editText = (EditText) findViewById(R.id.edit_text);

        //新建一个动态广播监听对象
        final BroadcastReceiver dynamicReceiver = new Dynamic_Receiver();
        final BroadcastReceiver dynamicWidgetReceiver = new App_Widget();

        //当点击注册广播按钮
        dynamicButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //如果之前没有注册,则注册
                if(dynamicButton.getText().equals("Register Broadcast"))
                {
                    //生成一个过滤器并注册该过滤器
                    IntentFilter dynamic_filter = new IntentFilter();
                    dynamic_filter.addAction(DYNAMICACTION);
                    registerReceiver(dynamicReceiver, dynamic_filter);
                    registerReceiver(dynamicWidgetReceiver, dynamic_filter);

                    //更改按钮的内容为取消注册
                    dynamicButton.setText(R.string.dynamic_unregister_button);
                }
                //如果之前注册过,则取消注册
                else
                {
                    unregisterReceiver(dynamicReceiver);
                    unregisterReceiver(dynamicWidgetReceiver);

                    //更改按钮的内容为注册
                    dynamicButton.setText(R.string.dynamic_register_button);
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //获取输入框的文本
                String text = editText.getText().toString();

                //将要传递的通知文本加入到bundle中
                Bundle bundle = new Bundle();
                bundle.putString("Content", text);

                //发送广播
                Intent intent = new Intent(DYNAMICACTION);
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
        });
    }
}