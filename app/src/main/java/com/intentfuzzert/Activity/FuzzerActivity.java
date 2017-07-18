package com.intentfuzzert.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.intentfuzzert.R;
import com.intentfuzzert.bean.ExtendSer;
import com.intentfuzzert.bean.SerializableTest;

import java.util.ArrayList;
import java.util.List;

import static com.intentfuzzert.R.string.expored;

public class FuzzerActivity extends AppCompatActivity {

    public String packagename = "";
    public String appname=null;
    private List componentlist = new ArrayList();
    private ListView mlistview;
    private Button button;
    private TextView textView;
    private TextView tvAppName;
    private TextView tvPkgName;
    private ImageView icon;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuzzer);
        //button = (Button) findViewById(R.id.attack);
       // textView=(TextView)findViewById(R.id.tvAppversionName);
        textView=(TextView)findViewById(R.id.tvtext);
        tvAppName=(TextView) findViewById(R.id.tvAppLabel);
        tvPkgName=(TextView) findViewById(R.id.tvPkgName);
        icon=(ImageView ) findViewById(R.id.imgApp);

        packagename = this.getIntent().getStringExtra("packagename");
        appname=this.getIntent().getStringExtra("appname");

        bitmap=this.getIntent().getParcelableExtra("appicon");
        icon.setImageBitmap(bitmap);


        tvAppName.setText(appname);
        tvPkgName.setText(packagename);
        textView.setText("该应用暴露组件如下所示--点击后进行测试！");


        //Toast.makeText(this, "应用包名为：" + packagename, Toast.LENGTH_LONG).show();//测试是否传过来包名
        //startAttack();
        getExportedComponent();
        ArrayAdapter<String> marrayadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, componentlist);
        mlistview = (ListView) findViewById(R.id.list_component);
        mlistview.setAdapter(marrayadapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String componentname = componentlist.get(position).toString();
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packagename, componentname));
                intent.putExtra("serializable_key",  new ExtendSer(true,1,"dhasiojkjkjkhkjhkjkjhjdhfoaisdhjoaishdoaishdiasdasdasoidsjhaoidaoidasdasdasdawdsadasd"));
                //intent.putExtra("serializable_key",  new SerializableTest());
                Toast.makeText(FuzzerActivity.this, intent.toString(), Toast.LENGTH_SHORT).show();
                if (componentname.endsWith("Activity")) {
                    startActivity(intent);
                } else if (componentname.endsWith("Service")) {
                    startService(intent);
                } else if (componentname.endsWith("Receiver")) {
                    sendBroadcast(intent);
                }
            }
        });
        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAttack();
            }
        });*/
    }

  /*  public void startAttack() {
        for (int i=0;i<componentlist.size();i++) {
            String componentname=componentlist.get(i).toString();
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(packagename, componentname));
            intent.putExtra("serializable_key", new SerializableTest());
            Toast.makeText(this, intent.toString(), Toast.LENGTH_SHORT).show();
            if (componentname.endsWith("Activity")) {
                startActivity(intent);
            } else if (componentname.endsWith("Service")) {
                startService(intent);
            } else if (componentname.endsWith("Receiver")) {
                sendBroadcast(intent);
            }

        }
    }*/
    public void getExportedComponent(){//查找当前应用的暴露的组件
        PackageManager pm=getPackageManager();
        int exportedCount=0;  //记录有几个暴露的组件
        try {
            PackageInfo packageInfo=pm.getPackageInfo(packagename,PackageManager.GET_ACTIVITIES);
            ActivityInfo[] activityInfo=packageInfo.activities;
            try{for (ActivityInfo activity:activityInfo){
                if(activity.exported){//判断组件是否暴露

                    Log.d("Exported",activity.name);
                    exportedCount++;
                    try{componentlist.add(activity.name);}catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Log.d("Not",activity.name);
                }
            }}catch (Exception e){
                e.printStackTrace();
            }
            ServiceInfo[] serviceInfos=pm.getPackageInfo(packagename,PackageManager.GET_SERVICES).services;
            try{for (ServiceInfo serviceInfo:serviceInfos){
                if (serviceInfo.exported){
                    exportedCount++;
                    Log.d("Exported",serviceInfo.name);
                    try{componentlist.add(serviceInfo.name);}catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    Log.d("Not",serviceInfo.name);
                }
            }}catch (Exception e){
                e.printStackTrace();
            }
            ActivityInfo[] receivers = pm.getPackageInfo(packagename, PackageManager.GET_RECEIVERS).receivers;
            try{for (ActivityInfo receiver:receivers){
                if (receiver.exported){
                    exportedCount++;
                    Log.d("Exported",receiver.name);
                    try{
                        componentlist.add(receiver.name);}catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    Log.d("Not",receiver.name);
                }
            }}catch (Exception e){
                e.printStackTrace();
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        if(exportedCount!=0)
        {
            Toast.makeText(FuzzerActivity.this, "该应用共有"+exportedCount+"个暴露组件", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(FuzzerActivity.this, "该应用没有暴露组件", Toast.LENGTH_SHORT).show();
        }
    }

}
