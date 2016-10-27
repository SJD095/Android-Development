package com.sunzhongyang.sjd.lab_project_four;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class Dynamic_Receiver extends BroadcastReceiver
{
    private static final String DYNAMICACTION = "com.sunzhongyang.sjd.lab_project_four.dynamicreceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //获取bundle及其中的通知文本
        Bundle bundle = intent.getExtras();
        String text = bundle.getString("Content");

        Notification.Builder builder = new Notification.Builder(context);

        //设置并显示一个通知
        builder.setContentTitle("动态广播")
                .setContentText(text)
                .setTicker("您有一条新消息")
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.dynamic))
                .setSmallIcon(R.mipmap.dynamic)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //点击通知则跳转到主界面
        Intent mintent = new Intent(context, Main_Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mintent, 0);
        builder.setContentIntent(pendingIntent);

        manager.notify(0, builder.build());
    }
}