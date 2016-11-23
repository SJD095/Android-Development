package com.sunzhongyang.sjd.lab_project_eight;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //不在应用中显示应用名
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //载入布局
        setContentView(R.layout.activity_add);

        //获取一个数据库
        myDB tmp_db = new myDB(this);
        final SQLiteDatabase current_db = tmp_db.get_db();

        //获取一个包含数据库全部信息的游标
        final Cursor full_cursor = current_db.rawQuery("select * from birthday", null);

        //获取页面控件,用于截取输入
        final EditText name_edittext = (EditText) findViewById(R.id.name_edittext);
        final EditText birthday_edittext = (EditText) findViewById(R.id.birthday_edittext);
        final EditText gift_edittext = (EditText) findViewById(R.id.gift_edittext);

        //获取增加按钮
        Button add_button = (Button) findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener()
        {
            //重写onClick,规定点击按钮执行的动作
            @Override
            public void onClick(View view)
            {
                //获取输入信息
                String name_content = name_edittext.getText().toString();
                String birthday_content = birthday_edittext.getText().toString();
                String gift_content = gift_edittext.getText().toString();

                //如果名字为空
                if(name_content.equals(""))
                {
                    Toast.makeText(AddActivity.this, "名字为空,请完善", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //如果名字重复
                    while (full_cursor.moveToNext())
                    {
                        if(name_content.equals(full_cursor.getString(1)))
                        {
                            Toast.makeText(AddActivity.this, "名字重复啦,请核查", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    //通过ContentValues在数据库中增加新内容
                    ContentValues cv = new ContentValues();
                    cv.put("name", name_content);
                    cv.put("birth", birthday_content);
                    cv.put("gift", gift_content);
                    current_db.insert("birthday", null, cv);

                    //活动结束,自动返回上一页面
                    finish();
                }
            }
        });
    }
}
