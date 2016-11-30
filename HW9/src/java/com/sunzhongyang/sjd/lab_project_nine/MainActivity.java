package com.sunzhongyang.sjd.lab_project_nine;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    //获取天气服务的url
    private static final String url = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //载入布局
        setContentView(R.layout.activity_main);

        //获取搜索键
        Button search_button = (Button) findViewById(R.id.serach_button);
        final EditText search_editview = (EditText) findViewById(R.id.search_editview);

        search_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //如果已经连接到网络
                if(isConnected(MainActivity.this))
                {
                    //保存所需要搜索的城市名
                    final String city_name = search_editview.getText().toString();
                    //如果城市名非空
                    if(!city_name.equals(""))
                    {
                        //新建一个线程执行网络访问
                        new Thread(){
                            @Override
                            public void run()
                            {
                                //建立并设置连接
                                HttpURLConnection connection = null;
                                try
                                {
                                    connection = (HttpURLConnection) ((new URL(url.toString()).openConnection()));
                                    connection.setRequestMethod("POST");
                                    connection.setReadTimeout(8000);
                                    connection.setConnectTimeout(8000);

                                    //向服务器发送信息
                                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                                    String request = URLEncoder.encode(city_name, "utf-8");

                                    out.writeBytes("theCityCode=" + request + "&theUserID=");

                                    //读取服务器返回的信息
                                    InputStream in = connection.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                    StringBuilder response = new StringBuilder();

                                    String line;

                                    while((line = reader.readLine()) != null)
                                    {
                                        response.append(line);
                                    }

                                    //向handler对象发送消息以传递数据
                                    Message message = new Message();
                                    message.what = 0;
                                    message.obj = parseXMLWithPull(response.toString());
                                    handler.sendMessage(message);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                //断开连接
                                finally
                                {
                                    if(connection != null)
                                    {
                                        connection.disconnect();
                                    }
                                }
                            }
                        }.start();
                    }
                }
                //如果没有连接到网络,则弹出窗口提示
                else
                {
                    Toast.makeText(MainActivity.this, "当前没有可用的网络!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //建立一个handler从线程接收数据,并根据情况决定UI
    private Handler handler = new Handler()
    {
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                //根据服务器返回消息类型决定对应的操作
                case 0:
                    ArrayList<String> list = (ArrayList<String>) message.obj;
                    if(list.get(0).equals("发现错误：免费用户24小时内访问超过规定数量。http://www.webxml.com.cn/"))
                    {
                        Toast.makeText(MainActivity.this, "免费用户24小时内访问超过规定数量50次", Toast.LENGTH_SHORT).show();
                    }
                    else if(list.get(0).equals("查询结果为空。http://www.webxml.com.cn/"))
                    {
                        Toast.makeText(MainActivity.this, "当前城市不存在,请重新输入", Toast.LENGTH_SHORT).show();
                    }
                    else if(list.get(0).equals("发现错误：免费用户不能使用高速访问。http://www.webxml.com.cn/"))
                    {
                        Toast.makeText(MainActivity.this, "您的点击速度过快,二次查询间隔<600ms", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //如果一切正常,则更新UI
                        updateUI(list);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //判断当前是否已连接到网络
    public boolean isConnected(Activity activity)
    {
        //获取网络连接状态
        Context context = MainActivity.this.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

        for (int i = 0; i < networkInfo.length; i++)
        {
            if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
            {
                return true;
            }
        }
        return false;
    }

    //将服务器返回的信息转换为字符串链表
    public ArrayList<String> parseXMLWithPull(String response)
    {
        ArrayList<String> list = new ArrayList<String>();

        //读取xml文本
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(response));

            //在文件截止前逐行读取
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                switch(eventType)
                {
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("string"))
                        {
                            String str = parser.nextText();
                            list.add(str);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return list;
    }

    //更新UI
    public void updateUI(ArrayList<String> list)
    {
        //获取各个控件并且根据list的内容初始化其内容
        LinearLayout detail_panel = (LinearLayout) findViewById(R.id.detail_panel);
        detail_panel.setVisibility(LinearLayout.VISIBLE);

        TextView textview_city_name = (TextView) findViewById(R.id.textview_city_name);
        textview_city_name.setText(list.get(1));

        TextView textview_update_time = (TextView) findViewById(R.id.textview_update_time);
        textview_update_time.setText(list.get(3).split(" ")[1] + " 更新");

        TextView textview_tem = (TextView) findViewById(R.id.textview_tem);
        textview_tem.setText(list.get(4).split("：")[2].split("；")[0]);

        TextView tem_range = (TextView) findViewById(R.id.tem_range);
        tem_range.setText(list.get(8));

        TextView shidu = (TextView) findViewById(R.id.shidu);
        shidu.setText(list.get(4).split("：")[4]);

        TextView kongqizhiliang = (TextView) findViewById(R.id.kongqizhiliang);
        kongqizhiliang.setText(list.get(5).split("：")[2].replace("。", ""));

        TextView fengli = (TextView) findViewById(R.id.fengli);
        fengli.setText(list.get(4).split("：")[3].split("；")[0]);

        TextView textview_zyx = (TextView) findViewById(R.id.textview_zyx);
        textview_zyx.setText(list.get(6).split("：")[1].split("。")[0]);

        TextView textview_ganmao = (TextView) findViewById(R.id.textview_ganmao);
        textview_ganmao.setText(list.get(6).split("：")[2].split("。")[0]);

        TextView textview_chuanyi = (TextView) findViewById(R.id.textview_chuanyi);
        textview_chuanyi.setText(list.get(6).split("：")[3].split("。")[0]);

        TextView textview_xiche = (TextView) findViewById(R.id.textview_xiche);
        textview_xiche.setText(list.get(6).split("：")[4].split("。")[0]);

        TextView textview_yundong = (TextView) findViewById(R.id.textview_yundong);
        textview_yundong.setText(list.get(6).split("：")[5].split("。")[0]);

        TextView date_one = (TextView) findViewById(R.id.date_one);
        date_one.setText(list.get(7));

        TextView weather_one = (TextView) findViewById(R.id.weather_one);
        weather_one.setText(list.get(8));

        TextView t_one = (TextView) findViewById(R.id.t_one);
        t_one.setText(list.get(9));

        TextView date_two = (TextView) findViewById(R.id.date_two);
        date_two.setText(list.get(12));

        TextView weather_two = (TextView) findViewById(R.id.weather_two);
        weather_two.setText(list.get(13));

        TextView t_two = (TextView) findViewById(R.id.t_two);
        t_two.setText(list.get(14));

        TextView date_three = (TextView) findViewById(R.id.date_three);
        date_three.setText(list.get(17));

        TextView weather_three = (TextView) findViewById(R.id.weather_three);
        weather_three.setText(list.get(18));

        TextView t_three = (TextView) findViewById(R.id.t_three);
        t_three.setText(list.get(19));

        TextView date_four = (TextView) findViewById(R.id.date_four);
        date_four.setText(list.get(22));

        TextView weather_four = (TextView) findViewById(R.id.weather_four);
        weather_four.setText(list.get(23));

        TextView t_four = (TextView) findViewById(R.id.t_four);
        t_four.setText(list.get(24));

        TextView date_five = (TextView) findViewById(R.id.date_five);
        date_five.setText(list.get(27));

        TextView weather_five = (TextView) findViewById(R.id.weather_five);
        weather_five.setText(list.get(28));

        TextView t_five = (TextView) findViewById(R.id.t_five);
        t_five.setText(list.get(29));
    }
}
