package com.sunzhongyang.sjd.second_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import android.view.*;

/*
13331233    孙中阳
2016.10.07  szy@sunzhongyang.com
 */
public class Second_Project extends Activity
{

    //重写onCreate()方法,初始化页面
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //不在应用中显示应用名
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //载入布局main_layout
        setContentView(R.layout.main_layout);

        //根据id获取main_layout中的登陆按钮和注册按钮
        Button login_Button = (Button) findViewById(R.id.loginButton);
        Button register_Button = (Button) findViewById(R.id.registerButton);

        //根据id获取main_layout中的用户名输入框和密码输入框
        final EditText username = (EditText) findViewById(R.id.userEditView);
        final EditText password = (EditText) findViewById(R.id.passwordEditView);

        //根据id获取main_layout中的单选按钮
        final RadioGroup radio_Group = (RadioGroup) findViewById(R.id.id0);

        //为登陆按钮注册监听按下事件
        login_Button.setOnClickListener(new View.OnClickListener()
        {
            //重写onClick,规定点击按钮执行的动作
            @Override
            public void onClick(View view)
            {
                //首先获取用户名输入框和密码输入框中的内容
                String usernameInput = username.getText().toString();
                String passwordInput = password.getText().toString();

                //如果用户名输入框为空,则弹窗提示
                if(TextUtils.isEmpty(usernameInput))
                {
                    Toast.makeText(Second_Project.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }
                //如果密码输入框为空,则弹窗提示
                else if(TextUtils.isEmpty(passwordInput))
                {
                    Toast.makeText(Second_Project.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                //如果用户名为Android,密码为123456则弹出对话框提示登陆成功
                else if(usernameInput.equals("Android") && passwordInput.equals("123456"))
                {
                    //使用Builder构造器
                    new AlertDialog.Builder(Second_Project.this)
                            .setTitle("提示")
                            .setMessage("登陆成功")
                            //点击确定按钮则弹框提示确定按钮被按下
                            .setPositiveButton("确定", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    Toast.makeText(Second_Project.this, "对话框“确定”按钮被点击", Toast.LENGTH_SHORT).show();
                                }
                            })
                            //点击取消安妮则提示取消按钮被按下
                            .setNegativeButton("取消", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    Toast.makeText(Second_Project.this, "对话框“取消”按钮被点击", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create()
                            .show();
                }
                //否则弹出对话框提示登录失败
                else
                {
                    new AlertDialog.Builder(Second_Project.this)
                            .setTitle("提示")
                            .setMessage("登陆失败")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    Toast.makeText(Second_Project.this, "对话框“确定”按钮被点击", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    Toast.makeText(Second_Project.this, "对话框“取消”按钮被点击", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create()
                            .show();
                }
            }
        });

        //点击注册按钮,弹框提示
        register_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //获取点击按钮时单选按钮组选中的按钮
                int id = radio_Group.getCheckedRadioButtonId();
                RadioButton radio_Button = (RadioButton) radio_Group.findViewById(id);

                //根据单选按钮的内容决定弹框提示的内容
                Toast.makeText(Second_Project.this, radio_Button.getText().toString() + "身份注册功能尚未开启", Toast.LENGTH_SHORT).show();
            }
        });

        //为单选按钮组注册监听更改事件
        radio_Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                //获取被选中的单选按钮
                RadioButton radio_Button = (RadioButton) radio_Group.findViewById(checkedId);

                //根据被选中单选按钮的内容决定弹框提示的内容
                Toast.makeText(Second_Project.this, radio_Button.getText().toString() + "身份被选中", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
