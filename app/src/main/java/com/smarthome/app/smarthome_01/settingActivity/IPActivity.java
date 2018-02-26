package com.smarthome.app.smarthome_01.settingActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smarthome.app.smarthome_01.R;
import com.smarthome.app.smarthome_01.util.SettingUtil;
import com.smarthome.app.smarthome_01.util.UIUtil;

/**
 * Created by maomao on 2018/1/13.
 */

public class IPActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView titleText;
    private Button backButton;
    private EditText ipEditText;
    private Button ipEdit, ipConfirm;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtil.transToolBar(IPActivity.this);
        setContentView(R.layout.activity_ip);

        titleText = (TextView) findViewById(R.id.Title_Text);
        backButton = (Button) findViewById(R.id.Back_Button);
        ipEditText = (EditText) findViewById(R.id.ip_editText);
        ipEdit = (Button) findViewById(R.id.ip_edit);
        ipConfirm = (Button) findViewById(R.id.ip_confirm);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        backButton.setOnClickListener(this);
        ipEdit.setOnClickListener(this);
        ipConfirm.setOnClickListener(this);
        ipConfirm.setEnabled(false);

        titleText.setText(R.string.ip_setting);
        ipEditText.setText(pref.getString("IP", null));
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ip_edit:
                editIP();
                break;
            case R.id.ip_confirm:
                confirmIP();
                break;
            case R.id.Back_Button:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 编辑ip地址
     */
    private void editIP()
    {
        ipEditText.setFocusable(true);
        ipEditText.setFocusableInTouchMode(true);
        ipEditText.requestFocus();
        //打开软键盘
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        ipEditText.setSelection(ipEditText.getText().length());
        ipEdit.setEnabled(false);
        ipConfirm.setEnabled(true);
    }

    /**
     * 确认输入的ip地址
     */
    private void confirmIP()
    {
        String ip = ipEditText.getText().toString();
        if (!isIPCorrect(ip))
        {
            UIUtil.showToast(IPActivity.this, "ip地址不正确，请重新输入！");
            return;
        }

        //存储ip地址，下次可直接读入
        editor = pref.edit();
        editor.putString("IP", ip);
        editor.apply();

        ipEdit.setEnabled(true);
        ipConfirm.setEnabled(false);

        //收起软键盘
        imm.hideSoftInputFromWindow(ipEditText.getWindowToken(), 0);

        SettingUtil.setIP(ip);
        ipEditText.setFocusable(false);
        ipEditText.setFocusableInTouchMode(false);
        UIUtil.showToast(IPActivity.this, "ip地址设置成功！");
    }

    /**
     * 用于判断输入的ip地址是否正确
     *
     * @param string ip字符串
     * @return ip是否正确
     */
    private boolean isIPCorrect(String string)
    {
        String ipPattern = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return string.matches(ipPattern);
    }
}
