package com.smarthome.app.smarthome_01.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by maomao on 2017/12/16.
 */

public class UIUtil
{
    private static Toast toast;
    private static ProgressDialog pd;

    /**
     * 解决无限Toast的封装方法
     *
     * @param context 上下文
     * @param desc    显示内容
     */
    public static void showToast(Context context, String desc)
    {
        if (toast == null)
        {
            toast = Toast.makeText(context, desc, Toast.LENGTH_SHORT);
        }
        else
        {
            toast.setText(desc);
        }
        toast.show();
    }

    /**
     * 显示进度对话框
     */
    public static void showDialog(Context context)
    {
        pd = new ProgressDialog(context);
        pd.setMessage("正在玩命加载中...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    /**
     * 关闭进度对话框
     */
    public static void cancelDialog()
    {
        if (pd != null)
        {
            pd.dismiss();
        }
    }

    /**
     * 状态栏透明化
     *
     * @param activity 需要进行美化的活动
     */
    public static void transToolBar(AppCompatActivity activity)
    {
        if (Build.VERSION.SDK_INT >= 21)
        {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                    .SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
