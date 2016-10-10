package com.sunzhongyang.sjd.third_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.*;
import android.view.*;

import com.sunzhongyang.sjd.third_project.R;

/*
13331233    孙中阳
2016.10.07  szy@sunzhongyang.com
 */

public class Third_Project extends Activity
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

        //根据id获取main_layout中的两个TextInputLayout
        final TextInputLayout usernameText = (TextInputLayout) findViewById(R.id.userEditView);
        final TextInputLayout passwordText = (TextInputLayout) findViewById(R.id.passwordEditView);

        //根据两个TextInputLayout获取用户名输入框和密码输入框
        final EditText username = usernameText.getEditText();
        final EditText password = passwordText.getEditText();

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

                //如果用户名输入框为空,则使用TextInputLayout的错误信息提示
                if(TextUtils.isEmpty(usernameInput))
                {
                    usernameText.setErrorEnabled(true);
                    usernameText.setError("用户名不能为空");
                }
                //如果密码输入框为空,则使用TextInputLayout的错误信息提示
                else if(TextUtils.isEmpty(passwordInput))
                {
                    passwordText.setErrorEnabled(true);
                    passwordText.setError("密码不能为空");
                }
                //如果用户名为Android,密码为123456则弹出Snackbar提示登陆成功
                else if(usernameInput.equals("Android") && passwordInput.equals("123456"))
                {
                    //使用Snackbar构造器
                    Snackbar.make(view.getRootView(), "登陆成功", Snackbar.LENGTH_SHORT)
                            .setAction("按钮", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(Third_Project.this, "Snackbar被点击", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
                //否则弹出Snackbar提示登录失败
                else
                {
                    Snackbar.make(view.getRootView(), "登录失败", Snackbar.LENGTH_SHORT)
                            .setAction("按钮", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(Third_Project.this, "Snackbar被点击", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
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

                //根据单选按钮的内容决定Snackbar提示的内容
                Snackbar.make(view.getRootView(), radio_Button.getText().toString() + "身份注册功能尚未开启", Snackbar.LENGTH_SHORT)
                        .setAction("按钮", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(Third_Project.this, "Snackbar被点击", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .setDuration(5000)
                        .show();
            }
        });

        //为单选按钮组注册监听更改事件
        radio_Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                //获取被选中的单选按钮
                RadioButton radio_Button = (RadioButton) radio_Group.findViewById(checkedId);


                //根据被选中单选按钮的内容决定Snackbar提示的内容
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), radio_Button.getText().toString() + "身份被选中", Snackbar.LENGTH_SHORT)
                        .setAction("按钮", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(Third_Project.this, "Snackbar被点击", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .setDuration(5000)
                        .show();
            }
        });
    }
}
