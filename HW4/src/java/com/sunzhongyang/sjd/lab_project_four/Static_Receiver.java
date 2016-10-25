package com.sunzhongyang.sjd.lab_project_four;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class Static_Receiver extends BroadcastReceiver
{
    private static final String STATICACTION = "com.sunzhongyang.sjd.lab_project_four.staticcreceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //如果获取到的广播是某个准备响应的广播
        if(intent.getAction().equals(STATICACTION))
        {
            //获取bundle及其中的信息
            Bundle bundle = intent.getExtras();
            String text = bundle.getString("Content");
            int PictureId = bundle.getInt("PictureId");

            //创建一个通知
            Notification.Builder builder = new Notification.Builder(context);

            builder.setContentTitle("静态广播")
                    .setContentText(text)
                    .setTicker("您有一条新消息")
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), PictureId))
                    .setSmallIcon(PictureId)
                    .setAutoCancel(true);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //设置点击通知为返回主界面
            Intent mintent = new Intent(context, Main_Activity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mintent, 0);
            builder.setContentIntent(pendingIntent);

            manager.notify(0, builder.build());
        }
    }
}