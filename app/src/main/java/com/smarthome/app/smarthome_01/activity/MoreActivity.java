package com.smarthome.app.smarthome_01.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.smarthome.app.smarthome_01.R;
import com.smarthome.app.smarthome_01.settingActivity.IPActivity;
import com.smarthome.app.smarthome_01.util.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoreActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private TextView titleText;
    private Button backButton;
    private List<Map<String, String>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtil.transToolBar(MoreActivity.this);
        setContentView(R.layout.activity_more);

        initList();
        SimpleAdapter adapter = new SimpleAdapter(MoreActivity.this, list, R.layout.item_more,
                new String[]{"item_name", "item_logo"}, new int[]{R.id.item_name, R.id.item_logo});
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        titleText = (TextView) findViewById(R.id.Title_Text);
        backButton = (Button) findViewById(R.id.Back_Button);
        titleText.setText(R.string.more);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void initList()
    {
        HashMap<String, String> map1 = new HashMap<>();
        HashMap<String, String> map2 = new HashMap<>();
        map1.put("item_name", "IP设置");
        map1.put("item_logo", ">");
        map2.put("item_name", "关于");
        map2.put("item_logo", ">");
        list.add(map1);
        list.add(map2);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        if(0 == i)
        {
            Intent IP_intent = new Intent(MoreActivity.this, IPActivity.class);
            startActivity(IP_intent);
        }
    }
}
