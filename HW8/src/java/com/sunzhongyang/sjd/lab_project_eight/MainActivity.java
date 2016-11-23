package com.sunzhongyang.sjd.lab_project_eight;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //不在应用中显示应用名
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //载入布局
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onResume()
    {
        //因为每次onResume()都会被执行而且从增加提示返回后也需要执行这一函数,所以这里包括了大部分内容
        super.onResume();

        //获得一个数据库
        myDB helper = new myDB(this);
        final SQLiteDatabase current_db = helper.get_db();

        //根据数据库获取一个有数据库全部内容的游标
        Cursor full_cursor = current_db.rawQuery("select * from birthday", null);

        //获取增加条目的按钮
        Button add_item_button = (Button) findViewById(R.id.add_item_button);

        //点击增加按钮,跳转到增加页面
        add_item_button.setOnClickListener(new View.OnClickListener()
        {
            //重写onClick,规定点击按钮执行的动作
            @Override
            public void onClick(View view)
            {
                //跳转到新页面
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });


        final List<Map<String, Object>> data = new ArrayList<>();

        //构造用于存储数据库中信息以初始化ListView的数组
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> birthdays = new ArrayList<String>();
        ArrayList<String> gifts = new ArrayList<String>();

        //将游标中数据库内的数据添加到数组中
        while (full_cursor.moveToNext())
        {
            names.add(full_cursor.getString(1));
            birthdays.add(full_cursor.getString(2));
            gifts.add(full_cursor.getString(3));
        }

        //使用数组初始化用于构造Listview的data
        for(int i = 0; i < names.size(); i++)
        {
            Map<String, Object> tmp = new LinkedHashMap<>();
            tmp.put("names", names.get(i));
            tmp.put("birthdays", birthdays.get(i));
            tmp.put("gifts", gifts.get(i));
            data.add(tmp);
        }

        //根据id获取ListView
        ListView contactList = (ListView) findViewById(R.id.listView);

        //构造ListView
        final SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.list_layout, new String[] {"names", "birthdays", "gifts"}, new int[] {R.id.list_name, R.id.list_birthday, R.id.list_gift});
        contactList.setAdapter(sAdapter);

        //为ListView的每一个Item注册短按事件监听器
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int i, long l)
            {
                //为弹出的对话框载入布局
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                View v = factory.inflate(R.layout.dialog_layout, null);

                final int count = i;

                //获取页面控件也设置数据库及获取输入
                TextView name_text = (TextView) v.findViewById(R.id.name_text);
                final EditText birthday_text = (EditText) v.findViewById(R.id.birthday_edit);
                final EditText gift_text = (EditText) v.findViewById(R.id.gift_edit);
                final TextView phone_text = (TextView) v.findViewById(R.id.phone_text);

                //显示被点击条目的名字
                name_text.setText(data.get(count).get("names").toString());
                birthday_text.setText(data.get(count).get("birthdays").toString());
                gift_text.setText(data.get(count).get("gifts").toString());

                //设置编辑框中默认的初始内容
                Editable text = birthday_text.getText();
                birthday_text.setSelection(text.length());

                Editable text2 = gift_text.getText();
                gift_text.setSelection(text2.length());

                //获取并设置该联系人的电话
                String phone_number = getPhoneNumber(data.get(count).get("names").toString());
                phone_text.setText(phone_number);

                //构造一个有布局的对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(v);
                builder.setTitle("此处应为表情?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j)
                    {
 //here tell something
                        //获取有关输入
                        String birthday_information = birthday_text.getText().toString();
                        String gift_information = gift_text.getText().toString();

                        //更新数据库
                        ContentValues cv = new ContentValues();
                        cv.put("birth", birthday_information);
                        cv.put("gift", gift_information);

                        String tmp = data.get(count).get("names").toString();
                        String[] args = {tmp};

                        current_db.update("birthday", cv, "name=?", args);

                        //更新ListView
                        data.get(count).put("gifts", gift_information);
                        data.get(count).put("birthdays", birthday_information);
                        sAdapter.notifyDataSetChanged();
                    }
                });
                //点击取消按钮则提示取消按钮被按下
                builder.setNegativeButton("否", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j)
                    {

                    }
                });
                builder.create();
                builder.show();
            }
        });

        //为ListView的每个Item设置长按动作
        contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int i, long l)
            {
                //用count替换 i 以避免与其他使用 i 的函数混淆
                final int count = i;

                //弹出一个提示框
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("是否删除")
                        //点击确定按钮则弹框提示确定按钮被按下
                        .setPositiveButton("是", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j)
                            {
                                current_db.execSQL("delete from birthday where name='" + data.get(count).get("names").toString() + "'");

                                //从数据源删除有关被选中联系人的数据,并通知Adapter有关更改
                                data.remove(count);
                                sAdapter.notifyDataSetChanged();
                            }
                        })
                        //点击取消按钮则提示取消按钮被按下
                        .setNegativeButton("否", new DialogInterface.OnClickListener()
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

    //获取某个联系人的电话号码
    public String getPhoneNumber(String name)
    {
        //默认为"无"
        String result = "无";
        ContentResolver contentResolver = this.getContentResolver();
        Cursor cursor = contentResolver.query(android.provider.ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        while(cursor.moveToNext())
        {
            if(name.equals(cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME))))
            {
                result = "";
                String id = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts._ID));
                Log.i("t", id);
                Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);

                //要考虑到有多个电话号码的情况
                while (c.moveToNext())
                {
                    result += c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + "  ";
                }
            }
        }

        return result;
    }
}
