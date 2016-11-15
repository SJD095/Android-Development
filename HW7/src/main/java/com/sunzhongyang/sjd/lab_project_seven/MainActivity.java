package com.sunzhongyang.sjd.lab_project_seven;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //获取SharedPreferences对象,从中可获得密码有关信息
        final SharedPreferences password_preferences=getSharedPreferences("password",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=password_preferences.edit();

        //如果之前保存过密码,也就是之前曾经登陆过
        if(password_preferences.contains("pas"))
        {
            //载入只需输入密码的布局
            setContentView(R.layout.activity_login);

            //获取登陆按钮和清空按钮
            Button login_ok_Button = (Button) findViewById(R.id.login_ok_button);
            Button login_clear_Button = (Button) findViewById(R.id.login_clear_button);

            //获取密码输入框
            final EditText password_login_edittext = (EditText) findViewById(R.id.password_login);

            //为登录按钮设置点击事件
            login_ok_Button.setOnClickListener(new View.OnClickListener()
            {
                //重写onClick,规定点击按钮执行的动作
                @Override
                public void onClick(View view)
                {
                    //如果密码输入框中的内容和之前保存的密码一致
                    if(password_login_edittext.getText().toString().equals(password_preferences.getString("pas", "")))
                    {
                        //打开新页面
                        Intent intent = new Intent(MainActivity.this, dataActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        //提示密码错误
                        Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //为清空按钮设置点击事件
            login_clear_Button.setOnClickListener(new View.OnClickListener()
            {
                //重写onClick,规定点击按钮执行的动作
                @Override
                public void onClick(View view)
                {
                    //清空密码输入框中的内容
                    password_login_edittext.setText("");
                }
            });
        }
        else
        {
            //载入需要重复确认密码的布局
            setContentView(R.layout.activity_main);

            //获取登陆按钮和清空按钮
            Button ok_Button = (Button) findViewById(R.id.ok_button);
            Button clear_Button = (Button) findViewById(R.id.clear_button);

            //获取密码输入框和密码确认框
            final EditText original_edittext = (EditText) findViewById(R.id.password_original);
            final EditText configure_edittext = (EditText) findViewById(R.id.password_configure);

            //为确认按钮设置点击事件
            ok_Button.setOnClickListener(new View.OnClickListener()
            {
                //重写onClick,规定点击按钮执行的动作
                @Override
                public void onClick(View view)
                {
                    //首先获取密码输入框和确认密码输入框中的内容
                    String originalInput = original_edittext.getText().toString();
                    String configureInput = configure_edittext.getText().toString();

                    //如果其中某个输入框为空,则弹窗提示
                    if(TextUtils.isEmpty(originalInput) || TextUtils.isEmpty(configureInput))
                    {
                        Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                    //如果密码输入框和确认密码输入框中的内容一致
                    else if(originalInput.equals(configureInput))
                    {
                        //保存密码
                        editor.putString("pas", configureInput);
                        editor.commit();

                        //打开新页面
                        Intent intent = new Intent(MainActivity.this, dataActivity.class);
                        startActivity(intent);
                    }
                    //否则弹出对话框提示登录失败
                    else
                    {
                        Toast.makeText(MainActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //清空密码输入框和确认密码输入框中的内容
            clear_Button.setOnClickListener(new View.OnClickListener()
            {
                //重写onClick,规定点击按钮执行的动作
                @Override
                public void onClick(View view)
                {
                    original_edittext.setText("");
                    configure_edittext.setText("");
                }
            });
        }
    }
}
