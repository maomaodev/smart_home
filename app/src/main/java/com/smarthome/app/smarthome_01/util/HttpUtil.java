package com.smarthome.app.smarthome_01.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by maomao on 2017/8/28.
 */

public class HttpUtil
{
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                HttpURLConnection connection = null;
                try
                {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    System.out.println(connection.getResponseCode());
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        response.append(line);
                    }

                    if (null != listener)
                        listener.onFinish(response.toString());
                }
                catch (Exception e)
                {
                    if (null != listener)
                        listener.onError(e);
                } finally
                {
                    if (null != connection)
                        connection.disconnect();
                }
            }
        }).start();
    }

    public static void sendXMLData(final String address, final String xmlData, final
    HttpCallbackListener listener)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                HttpURLConnection connection = null;
                try
                {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(xmlData);
                    out.flush();
                    out.close();

                    System.out.println(connection.getResponseCode());
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        response.append(line);
                    }

                    if (null != listener)
                        listener.onFinish(response.toString());
                }
                catch (Exception e)
                {
                    if (null != listener)
                        listener.onError(e);
                } finally
                {
                    if (null != connection)
                        connection.disconnect();
                }
            }
        }).start();
    }

    /**
     * 判断是否有网络连接
     */
    public static boolean isNetworkConnected(Context context)
    {
        if (context != null)
        {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null)
            {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
