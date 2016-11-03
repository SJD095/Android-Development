package com.sunzhongyang.sjd.lab_project_six;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

/*
13331233    孙中阳
2016.11.3  szy@sunzhongyang.com
*/

public class MusicService extends Service
{
    //获取一个播放媒体的对象
    public MediaPlayer mp = new MediaPlayer();
    //获取一个和调用服务的Activity通信的对象
    public final IBinder binder = new Mybinder();
    //保存音乐文件的路径
    private String mediaPosition;

    public MusicService()
    {
        //获取音乐文件的路径
        mediaPosition = Environment.getExternalStorageDirectory() + "/data/K.Will-Melt.mp3";

        try
        {
            //为播放媒体的对象指定要播放的媒体
            mp.setDataSource(mediaPosition);
            //进入准备播放的状态
            mp.prepare();
            //单曲循环
            mp.setLooping(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void play()
    {
        //如果正在播放,则暂停播放
        if(mp.isPlaying())
        {
            mp.pause();
        }
        //如果没有播放,则开始播放
        else
        {
            mp.start();
        }
    }

    public void stop()
    {
        if(mp != null)
        {
            //停止播放
            mp.stop();
            try
            {
                //进入准备播放的状态
                mp.prepare();
                //将播放位置重置为开始位置
                mp.seekTo(0);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    //返回音乐文件的位置
    public String getMediaPosition()
    {
        return mediaPosition;
    }

    //设置音乐播放位置
    public void setMusicPlayPosition(int i)
    {
        mp.seekTo(i);
    }

    //用于返回当前对象
    public class Mybinder extends Binder
    {
        MusicService getService()
        {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        //绑定成功后返回binder
        return binder;
    }
}
