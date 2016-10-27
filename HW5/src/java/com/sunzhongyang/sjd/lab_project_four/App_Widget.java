package com.sunzhongyang.sjd.lab_project_four;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import static android.R.style.Widget;

/**
 * Implementation of App Widget functionality.
 */
public class App_Widget extends AppWidgetProvider
{
    //静态广播和动态广播的名称
    private static final String STATICACTION = "com.sunzhongyang.sjd.lab_project_four.staticcreceiver";
    private static final String DYNAMICACTION = "com.sunzhongyang.sjd.lab_project_four.dynamicreceiver";

    //重写onUpdate()函数,使得点击Widget图片能够跳转到主界面
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        //一个跳转到主界面的Intent
        Intent clickInt = new Intent(context, Main_Activity.class);
        //设置一个PendingIntent
        PendingIntent pi = PendingIntent.getActivity(context, 0, clickInt, 0);

        //通过RemoteViews设置Widget中Image的点击事件
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        rv.setOnClickPendingIntent(R.id.widget_img_view, pi);
        appWidgetManager.updateAppWidget(appWidgetIds, rv);
    }

    @Override
    public void onEnabled(Context context)
    {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context)
    {
        // Enter relevant functionality for when the last widget is disabled
    }

    //重写onReceive()函数,分别对静态广播和动态广播作不同的处理
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);

        //获取控制Widget的两个对象
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        //如果传入的intent是静态广播STATICACTION
        if(intent.getAction().equals(STATICACTION))
        {
            //获取静态广播中包含的Widget图像信息和文本信息
            Bundle bundle = intent.getExtras();
            String text = bundle.getString("Content");
            int PictureId = bundle.getInt("PictureId");

            //根据获取到的图像信息和文本信息设置Widget的外观
            rv.setTextViewText(R.id.widget_message, text);
            rv.setImageViewResource(R.id.widget_img_view, PictureId);
        }
        //如果传入的intent是动态广播DYNAMICACTION
        else if(intent.getAction().equals(DYNAMICACTION))
        {
            //获取动态广播中包含的Widget文本信息
            Bundle dynamicBundle = intent.getExtras();
            String dynamicText = dynamicBundle.getString("Content");

            //根据获取到的文本信息设置Widget的外观
            rv.setTextViewText(R.id.widget_message, dynamicText);
            rv.setImageViewResource(R.id.widget_img_view, R.drawable.dynamic);
        }

        //获取要更新的Widget的id并更新Widget
        ComponentName name = new ComponentName(context.getPackageName(), App_Widget.class.getName());
        int[] ids = appWidgetManager.getAppWidgetIds(name);
        appWidgetManager.updateAppWidget(ids, rv);
    }
}