package com.smarthome.app.smarthome_01.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class TAHActivity extends AppCompatActivity
{
    private TextView titleText;
    private Button backButton;
    private TextView tempText, humiText;
    private String IP = SettingUtil.getIP();

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Bundle bundle = msg.getData();
            tempText.setText(bundle.getString("temp") + "°C");
            humiText.setText(bundle.getString("humi"));
            HttpUtil.sendHttpRequest("http://" + IP + ":8080/test/ServletTAH", listener);
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
            UIUtil.showToast(TAHActivity.this, "无法连接服务器");
        }
    };

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtil.transToolBar(TAHActivity.this);
        setContentView(R.layout.activity_tah);

        tempText = (TextView) findViewById(R.id.tempText);
        humiText = (TextView) findViewById(R.id.humiText);
        titleText = (TextView) findViewById(R.id.Title_Text);
        backButton = (Button) findViewById(R.id.Back_Button);
        titleText.setText(R.string.TAH);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        HttpUtil.sendHttpRequest("http://" + IP + ":8080/test/ServletTAH", listener);
    }

    //使用Pull解析
    private void parseXMLWithPull(String xmlData)
    {
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();

            String temp = "", humi = "";
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                String nodeName = xmlPullParser.getName();
                switch (eventType)
                {
                    //开始解析某个结点
                    case XmlPullParser.START_TAG:
                    {
                        if ("temp".equals(nodeName))
                            temp = xmlPullParser.nextText();
                        else if ("humi".equals(nodeName))
                            humi = xmlPullParser.nextText();
                        break;
                    }
                    //完成解析某个结点
                    case XmlPullParser.END_TAG:
                    {
                        if ("TAH".equals(nodeName))
                        {
                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("temp", temp);
                            bundle.putString("humi", humi);
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
