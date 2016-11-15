package com.sunzhongyang.sjd.lab_project_seven;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class dataActivity extends AppCompatActivity
{
    //命名存储文本内容的文件
    String FILE_NAME = "my_content";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //载入布局
        setContentView(R.layout.activity_data);

        //获取三个按钮
        Button save_Button = (Button) findViewById(R.id.save_button);
        Button load_Button = (Button) findViewById(R.id.load_button);
        Button data_clear_Button = (Button) findViewById(R.id.data_clear_button);

        //获取文本输入框
        final EditText data_edittext = (EditText) findViewById(R.id.data_edittext);

        save_Button.setOnClickListener(new View.OnClickListener()
        {
            //重写onClick,规定点击按钮执行的动作
            @Override
            public void onClick(View view)
            {
                //向文件写入内容
                try (FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE))
                {
                    //获取文本输入框中的内容
                    String str = data_edittext.getText().toString();
                    //获取Bytes数组并写入文件
                    fileOutputStream.write(str.getBytes());

                    //提示写入成功
                    Toast.makeText(dataActivity.this, "save successfully", Toast.LENGTH_SHORT).show();
                }
                catch (IOException ex)
                {
                    Log.e("TAG", "Fail to save file.");
                }
            }
        });

        load_Button.setOnClickListener(new View.OnClickListener()
        {
            //重写onClick,规定点击按钮执行的动作
            @Override
            public void onClick(View view)
            {
                //从文件中读取内容
                try (FileInputStream fileInputStream = openFileInput(FILE_NAME))
                {
                    //从文件中读取出一个bytes数组
                    byte[] contents = new byte[fileInputStream.available()];
                    fileInputStream.read(contents);

                    //将数组转化为字符串
                    String result = new String(contents);
                    data_edittext.setText(result);

                    //这里可以说一说
                    //设置光标位置为文本结尾处
                    Editable text = data_edittext.getText();
                    data_edittext.setSelection(text.length());

                    //提示载入成功
                    Toast.makeText(dataActivity.this, "Load successfully", Toast.LENGTH_SHORT).show();
                }
                catch (IOException ex)
                {
                    //如果失败则提示
                    Toast.makeText(dataActivity.this, "Fail to load file", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "Fail to read file.");
                }
            }
        });

        data_clear_Button.setOnClickListener(new View.OnClickListener()
        {
            //重写onClick,点击按钮清空文本输入框
            @Override
            public void onClick(View view)
            {
                data_edittext.setText("");
            }
        });
    }
}
