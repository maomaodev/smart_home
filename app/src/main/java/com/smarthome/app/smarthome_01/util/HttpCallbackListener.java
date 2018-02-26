package com.smarthome.app.smarthome_01.util;

/**
 * Created by maomao on 2017/8/28.
 */

public interface HttpCallbackListener
{
    void onFinish(String response);

    void onError(Exception e);
}
