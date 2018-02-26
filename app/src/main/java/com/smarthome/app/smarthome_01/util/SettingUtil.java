package com.smarthome.app.smarthome_01.util;

/**
 * Created by maomao on 2018/1/12.
 */

public class SettingUtil
{
    public static String IP = null;

    public static void setIP(String IP)
    {
        SettingUtil.IP = IP;
    }

    public static String getIP()
    {
        return IP;
    }
}
