package com.sunzhongyang.sjd.lab_project_six;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/*
13331233    孙中阳
2016.11.2  szy@sunzhongyang.com
 */

public class MainActivity extends Activity
{
    //通过ServiceConnection由IBinder获取到的MusicService对象
    private MusicService musicService;

    //位于函数局部作用域外的几个控件可使得不同函数都能访问到这些控件,避免在重复执行的线程中逐次获取
    ImageView backgroundImage;
    SeekBar progressBar;
    TextView playTime;
    TextView musicTime;

    //设置一个布尔变量用于表示是否正在拖动滑动条,这样可保证拖动滑动条时滑动条不会因为Handler线程不断闪烁
    boolean onProgressBarChanged = false;

    //获取一个用于更新UI线程的Handler
    Handler handler = new Handler();

    //更新UI线程的具体实现
    Runnable update_thread = new Runnable()
    {
        @Override
        public void run()
        {
            //获取一个SimpleDateFormat对象,直接将int格式表示的时间转换为 mm:ss 格式
            SimpleDateFormat time = new SimpleDateFormat("mm:ss");

            //如果正在播放音乐
            if(musicService.mp.isPlaying())
            {
                //背景图片会在当前基础上顺时针转动1度
                backgroundImage.setRotation(backgroundImage.getRotation() + 1);
            }

            //根据音乐播放进度设置已播放时长
            playTime.setText(time.format(musicService.mp.getCurrentPosition()));
            //根据音乐信息设置音乐完整时长
            musicTime.setText(time.format(musicService.mp.getDuration()));

            //如果当前没有在拖动进度条
            if(!onProgressBarChanged)
            {
                //设置进度条最大值,使得进度条能够按照播放比例正常显示
                progressBar.setMax(musicService.mp.getDuration());
                //通过目前音乐播放位置设置进度条
                progressBar.setProgress(musicService.mp.getCurrentPosition());
            }

            //通过Handler使上述过程每100毫秒执行一次
            handler.postDelayed(update_thread, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //建立一个ServiceConnection连接
        connection();

        //不在应用中显示应用名
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //载入布局main_layout
        setContentView(R.layout.main_activity);

        //获取背景图片
        backgroundImage = (ImageView) findViewById(R.id.background_img);

        //获取下方的三个按钮
        final Button startButton = (Button) findViewById(R.id.button_left);
        Button stopButton = (Button) findViewById(R.id.button_middle);
        Button quitButton = (Button) findViewById(R.id.button_right);

        //获取展示播放状态的TextView
        final TextView statusTextView = (TextView) findViewById(R.id.player_status);

        //获取用于展示音乐已播放时长和总时长的TextView
        playTime = (TextView) findViewById(R.id.play_time);
        musicTime = (TextView) findViewById(R.id.music_time);

        //用于展示和设置播放进度的进度条
        progressBar = (SeekBar) findViewById(R.id.seek_bar);

        //为左侧按钮设置监听事件
        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //使用MusicService对象播放音乐

                musicService.play();

                //如果当前没有播放音乐,则更改按钮内容为PAUSE,播放状态为Playing
                if(startButton.getText().equals(getResources().getString(R.string.button_left_play)))
                {
                    startButton.setText(getResources().getString(R.string.button_left_pause));
                    statusTextView.setText(getResources().getString(R.string.status_play));
                }
                //如果当前正在播放音乐,则更改按钮内容为PLAY,播放状态为Stopped
                else
                {
                    startButton.setText(getResources().getString(R.string.button_left_play));
                    statusTextView.setText(getResources().getString(R.string.status_stop));
                }
            }
        });

        //为中间按钮设置监听事件
        stopButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //通过MusicService停止播放音乐
                musicService.stop();

                //更改按钮内容为PLAY,播放状态为IDLE
                startButton.setText(getResources().getString(R.string.button_left_play));
                statusTextView.setText(getResources().getString(R.string.status_init));
            }
        });

        //为右侧按钮设置监听状态
        quitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //停止更新UI的线程
                handler.removeCallbacks(update_thread);
                //和已建立连接的服务取消连接
                unbindService(sc);
                try
                {
                    //结束当前任务
                    MainActivity.this.finish();
                    System.exit(0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        //为进度条设置监听事件
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar s, int i, boolean b)
            {

            }

            @Override
            public void onStartTrackingTouch(SeekBar s)
            {
                //开始滑动后,滑动状态被设置为true
                onProgressBarChanged = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar s)
            {
                //停止滑动后,滑动状态被设置为false
                onProgressBarChanged = false;

                //通过MusicService更新音乐的播放位置
                musicService.setMusicPlayPosition(progressBar.getProgress());
            }
        });
    }

    //和Service建立连接
    private void connection()
    {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, sc, BIND_AUTO_CREATE);
    }

    //通过ServiceConnection获取MusicService对象
    private ServiceConnection sc = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            musicService = ((MusicService.Mybinder)(service)).getService();

            //通过音乐信息设置用于显示音乐路径的TextView
            TextView mediaPositionTextView = (TextView) findViewById(R.id.media_position);
            mediaPositionTextView.setText("Loaded media file: " + musicService.getMediaPosition());

            //为进度条设置最大值
            progressBar.setMax(musicService.mp.getDuration());

            //开始UI更新线程
            handler.post(update_thread);
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            //结束连接则将musicService对象置空
            musicService = null;
        }
    };
}
