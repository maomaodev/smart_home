package com.smarthome.app.smarthome_01.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.smarthome.app.smarthome_01.R;
import com.smarthome.app.smarthome_01.util.SettingUtil;
import com.smarthome.app.smarthome_01.util.UIUtil;

/**
 * Created by maomao on 2017/8/28.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button backButton;
    private Button tahButton, ledButton, videoButton, moreButton;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtil.transToolBar(MainActivity.this);
        setContentView(R.layout.activity_main);

        tahButton = (Button) findViewById(R.id.TAH_Button);
        ledButton = (Button) findViewById(R.id.LED_Button);
        videoButton = (Button) findViewById(R.id.video_Button);
        moreButton = (Button) findViewById(R.id.more_Button);
        backButton = (Button) findViewById(R.id.Back_Button);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        //注册按钮监听器
        tahButton.setOnClickListener(this);
        ledButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);
        moreButton.setOnClickListener(this);
        //返回按钮设置为不可见
        backButton.setVisibility(View.GONE);

        //从SharedPreferences中读取存储的ip地址
        String ip = pref.getString("IP", null);
        //记录ip地址变量，用于后续的网络连接操作
        SettingUtil.setIP(ip);
    }

    /**
     * 按钮监听动作
     *
     * @param view
     */
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.TAH_Button:
                startTAHActivity();
                break;
            case R.id.LED_Button:
                startLEDActivity();
                break;
            case R.id.video_Button:
                startVideoActivity();
                break;
            case R.id.more_Button:
                startMoreActivity();
                break;
            default:
                break;
        }
    }

    /**
     * 温湿度按钮监听动作
     */
    private void startTAHActivity()
    {
        if (SettingUtil.IP != null)
        {
            Intent TAH_intent = new Intent(MainActivity.this, TAHActivity.class);
            startActivity(TAH_intent);
        }
        else
        {
            UIUtil.showToast(MainActivity.this, "请先设置ip地址");
        }
    }

    /**
     * 灯光按钮监听动作
     */
    private void startLEDActivity()
    {
        if (SettingUtil.IP != null)
        {
            Intent LED_intent = new Intent(MainActivity.this, LEDActivity.class);
            startActivity(LED_intent);
        }
        else
        {
            UIUtil.showToast(MainActivity.this, "请先设置ip地址");
        }
    }

    /**
     * 视频监控按钮监听动作
     */
    private void startVideoActivity()
    {
        if (SettingUtil.IP != null)
        {
            Intent Video_intent = new Intent(MainActivity.this, VideoActivity.class);
            startActivity(Video_intent);
        }
        else
        {
            UIUtil.showToast(MainActivity.this, "请先设置ip地址");
        }
    }

    /**
     * 设置按钮监听动作
     */
    private void startMoreActivity()
    {
        Intent More_intent = new Intent(MainActivity.this, MoreActivity.class);
        startActivity(More_intent);
    }
}
