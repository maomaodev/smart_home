package com.smarthome.app.smarthome_01.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarthome.app.smarthome_01.R;
import com.smarthome.app.smarthome_01.util.HttpCallbackListener;
import com.smarthome.app.smarthome_01.util.HttpUtil;
import com.smarthome.app.smarthome_01.util.SettingUtil;
import com.smarthome.app.smarthome_01.util.UIUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by maomao on 2017/8/28.
 */

public class LEDActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView titleText;
    private Button backButton;
    private TextView LED1_Text, LED2_Text;
    private Button button1, button2;
    private ImageView LED1_View, LED2_View;

    private int STATE_LED1 = 0, STATE_LED2 = 0; //灯的状态，0表示灭，1表示亮
    private String IP = SettingUtil.getIP();    //用于网络连接的ip地址

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            STATE_LED1 = msg.arg1;
            STATE_LED2 = msg.arg2;

            if (0 == msg.arg1)
            {
                LED1_Text.setText("灭");
                button1.setBackgroundResource(R.drawable.switch_off);
                LED1_View.setImageResource(R.drawable.led_off);
            }
            else if (1 == msg.arg1)
            {
                LED1_Text.setText("亮");
                button1.setBackgroundResource(R.drawable.switch_on);
                LED1_View.setImageResource(R.drawable.led_on);
            }

            if (0 == msg.arg2)
            {
                LED2_Text.setText("灭");
                button2.setBackgroundResource(R.drawable.switch_off);
                LED2_View.setImageResource(R.drawable.led_off);
            }
            else if (1 == msg.arg2)
            {
                LED2_Text.setText("亮");
                button2.setBackgroundResource(R.drawable.switch_on);
                LED2_View.setImageResource(R.drawable.led_on);
            }
        }
    };

    private HttpCallbackListener listener = new HttpCallbackListener()
    {
        @Override
        public void onFinish(String response)
        {
            parseXMLWithPull(response);
        }

        @Override
        public void onError(Exception e)
        {
            UIUtil.showToast(LEDActivity.this, "无法连接服务器");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtil.transToolBar(LEDActivity.this);
        setContentView(R.layout.activity_led);

        LED1_Text = (TextView) findViewById(R.id.LED1_Text);
        LED2_Text = (TextView) findViewById(R.id.LED2_Text);
        button1 = (Button) findViewById(R.id.button_1);
        button2 = (Button) findViewById(R.id.button_2);
        titleText = (TextView) findViewById(R.id.Title_Text);
        backButton = (Button) findViewById(R.id.Back_Button);
        LED1_View = (ImageView) findViewById(R.id.led1_View);
        LED2_View = (ImageView) findViewById(R.id.led2_View);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        backButton.setOnClickListener(this);
        titleText.setText(R.string.LED);

        //发送网络请求获取当前灯的状态
        HttpUtil.sendHttpRequest("http://" + IP + ":8080/test/Servlet", listener);

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
            case R.id.button_1:
                switch_LED1();
                break;
            case R.id.button_2:
                switch_LED2();
                break;
            case R.id.Back_Button:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 使用Pull解析返回的xml数据
     *
     * @param xmlData 返回的xml数据
     */
    private void parseXMLWithPull(String xmlData)
    {
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();

            String LED1 = "", LED2 = "";
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                String nodeName = xmlPullParser.getName();
                switch (eventType)
                {
                    //开始解析某个结点
                    case XmlPullParser.START_TAG:
                    {
                        if ("LED1".equals(nodeName))
                            LED1 = xmlPullParser.nextText();
                        else if ("LED2".equals(nodeName))
                            LED2 = xmlPullParser.nextText();
                        break;
                    }
                    //完成解析某个结点
                    case XmlPullParser.END_TAG:
                    {
                        if ("LED".equals(nodeName))
                        {
                            Message message = new Message();
                            message.arg1 = Integer.parseInt(LED1);
                            message.arg2 = Integer.parseInt(LED2);
                            handler.sendMessage(message);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 灯1的打开和关闭操作
     */
    private void switch_LED1()
    {
        if (0 == STATE_LED1)
        {
            String xmlData = "LED1=1&LED2=" + String.valueOf(STATE_LED2);
            HttpUtil.sendXMLData("http://" + IP + ":8080/test/Servlet", xmlData, listener);
            UIUtil.showToast(LEDActivity.this, "灯1已打开！");
            //            UIUtil.showDialog(LEDActivity.this);
        }
        else if (1 == STATE_LED1)
        {
            String xmlData = "LED1=0&LED2=" + String.valueOf(STATE_LED2);
            HttpUtil.sendXMLData("http://" + IP + ":8080/test/Servlet", xmlData, listener);
            UIUtil.showToast(LEDActivity.this, "灯1已关闭！");
            //            UIUtil.showDialog(LEDActivity.this);
        }
    }

    /**
     * 灯2的打开和关闭操作
     */
    private void switch_LED2()
    {
        if (0 == STATE_LED2)
        {
            String xmlData = "LED2=1&LED1=" + String.valueOf(STATE_LED1);
            HttpUtil.sendXMLData("http://" + IP + ":8080/test/Servlet", xmlData, listener);
            UIUtil.showToast(LEDActivity.this, "灯2已打开！");
            //            UIUtil.showDialog(LEDActivity.this);
        }
        else if (1 == STATE_LED2)
        {
            String xmlData = "LED2=0&LED1=" + String.valueOf(STATE_LED1);
            HttpUtil.sendXMLData("http://" + IP + ":8080/test/Servlet", xmlData, listener);
            UIUtil.showToast(LEDActivity.this, "灯2已关闭！");
            //            UIUtil.showDialog(LEDActivity.this);
        }
    }

}
