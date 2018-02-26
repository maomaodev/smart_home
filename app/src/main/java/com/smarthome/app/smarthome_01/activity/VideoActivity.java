package com.smarthome.app.smarthome_01.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.smarthome.app.smarthome_01.R;
import com.smarthome.app.smarthome_01.util.SettingUtil;
import com.smarthome.app.smarthome_01.util.UIUtil;

import java.io.IOException;

/**
 * Created by maomao on 2017/8/29.
 */

public class VideoActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView titleText;
    private Button backButton;
    private SurfaceView surfaceView;
    private SeekBar seekBar;
    private Button playButton, fullButton;
    private TextView currentText, durationText;
    private LinearLayout seekBarView;
    private RelativeLayout titleView;
    private Toolbar toolbarView;

    private MediaPlayer mediaPlayer;
    private Handler handler;

    private boolean isPlaying = true;   //视频是否正在播放
    private boolean isAppear = true;    //进度条是否出现
    private float downX, downY;
    private int appearTime = 0;
    private Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtil.transToolBar(VideoActivity.this);
        setContentView(R.layout.activity_video);

        titleText = (TextView) findViewById(R.id.Title_Text);
        backButton = (Button) findViewById(R.id.Back_Button);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        currentText = (TextView) findViewById(R.id.current_text);
        durationText = (TextView) findViewById(R.id.duration_text);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        playButton = (Button) findViewById(R.id.play_button);
        fullButton = (Button) findViewById(R.id.full_button);
        seekBarView = (LinearLayout) findViewById(R.id.seekBar_view);
        titleView = (RelativeLayout) findViewById(R.id.title_view);
        toolbarView = (Toolbar) findViewById(R.id.toolbar_view);

        backButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        fullButton.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(changeListener);
        surfaceView.getHolder().addCallback(callback);
        uri = Uri.parse("http://" + SettingUtil.getIP() + ":8080/test/movie1.mp4");

        titleText.setText(R.string.video);
        handler = new Handler();
        mediaPlayer = new MediaPlayer();
        initMediaPlayerListener();
        initSurfaceViewListener();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.play_button:
                play();
                break;
            case R.id.full_button:
                full();
                break;
            case R.id.Back_Button:
                mediaPlayer.stop();
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 视频播放和暂停的状态操作
     */
    private void play()
    {
        if (isPlaying)
        {
            mediaPlayer.pause();
            isPlaying = false;
            playButton.setBackgroundResource(R.drawable.play);
        }
        else
        {
            mediaPlayer.start();
            isPlaying = true;
            playButton.setBackgroundResource(R.drawable.pause);
        }
    }

    /**
     * 视频横屏和竖屏的状态操作
     */
    private void full()
    {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            //变成竖屏
            toolbarView.setVisibility(View.VISIBLE);
            titleView.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else if (getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_PORTRAIT)
        {
            //变成横屏
            toolbarView.setVisibility(View.GONE);
            titleView.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * 监听手机的返回键
     */
    @Override
    public void onBackPressed()
    {
        //如果是横屏状态下返回，则变为竖屏状态
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            titleView.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //如果是竖屏状态返回，则直接退出当前界面
        else if (getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_PORTRAIT)
        {
            mediaPlayer.stop();
            finish();
        }
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback()
    {
        // 当surfaceView被创建完成之前才能绘制画布,所以只能在此回调方法之后开始播放
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder)
        {
            try
            {
                // 指定播放源
                mediaPlayer.setDataSource(VideoActivity.this, uri);
                // 将mediaPlayer和surfaceView时行绑定
                mediaPlayer.setDisplay(surfaceHolder);
                // 准备进行异步播放(当prepareAsync被调用后会执行mediaPlayer的onPrepared回调方法)
                mediaPlayer.prepareAsync();
                // 播放时屏幕保持唤醒
                mediaPlayer.setScreenOnWhilePlaying(true);
            }
            catch (IOException e)
            {
                UIUtil.showToast(VideoActivity.this, "无法连接服务器！");
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2)
        {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder)
        {
            if (mediaPlayer != null && mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    };

    private void initSurfaceViewListener()
    {
        //监听屏幕点击事件，用于隐藏和弹出进度条
        surfaceView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (isAppear)
                {
                    isAppear = false;
                    seekBarView.setVisibility(View.GONE);
                }
                else
                {
                    isAppear = true;
                    appearTime = 0;
                    seekBarView.setVisibility(View.VISIBLE);
                }
            }
        });

        //监听屏幕触摸事件，用于快进或快退
        surfaceView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float distanceX = motionEvent.getX() - downX;
                        float distanceY = motionEvent.getY() - downY;
                        int current = mediaPlayer.getCurrentPosition();
                        //  播放进度调节
                        if (Math.abs(distanceY) < 50 && distanceX > 100)
                        {
                            mediaPlayer.seekTo(current + 15000);    // 快进
                        }
                        else if (Math.abs(distanceY) < 50 && distanceX < -100)
                        {
                            mediaPlayer.seekTo(current - 15000);    // 快退
                        }
                        break;
                }

                return false;
            }
        });
    }

    /**
     * 初始化mediaPlayer
     */
    private void initMediaPlayerListener()
    {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                // 开始播放视频
                mediaPlayer.start();
                // 设置总时长
                durationText.setText(getTime(mp.getDuration()));
                currentText.setText(getTime(mp.getCurrentPosition()));
                seekBar.setMax(mp.getDuration());
                updateView();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                mediaPlayer.start();
            }
        });
    }

    private SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener()
    {
        // 当进度条停止修改的时候触发
        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {
            // 取得当前进度条的刻度
            int progress = seekBar.getProgress();
            if (mediaPlayer != null && mediaPlayer.isPlaying())
            {
                // 设置当前播放的位置
                mediaPlayer.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
        }
    };

    /**
     * 更新播放进度的递归
     */
    private void updateView()
    {
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // 设置进度控件
                if (mediaPlayer != null && mediaPlayer.isPlaying())
                {
                    currentText.setText(getTime(mediaPlayer.getCurrentPosition()));
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }

                // 计时用于隐藏进度条
                if (appearTime < 8)
                {
                    appearTime++;
                }
                else
                {
                    seekBarView.setVisibility(View.GONE);
                }
                updateView();
            }
        }, 1000);
    }

    private String getTime(int time)
    {
        String strTime = "";

        time /= 1000;
        //判断时间的分时刻
        if (time / 60 < 10)
        {
            strTime = "0" + time / 60;
        }
        else
        {
            strTime += time / 60;
        }

        //判断时间的秒时刻
        if (time % 60 < 10)
        {
            strTime += ":0" + time % 60;
        }
        else
        {
            strTime += ":" + time % 60;
        }

        return strTime;
    }
}
