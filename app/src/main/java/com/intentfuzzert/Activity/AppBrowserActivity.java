package com.intentfuzzert.Activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.intentfuzzert.R;
import com.intentfuzzert.bean.AppBrowserAdapter;
import com.intentfuzzert.bean.AppInfomation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppBrowserActivity extends AppCompatActivity {
    private ListView listView;
    private PackageManager pm;
    private Bitmap bitmap;
    private ProgressBar progressBar = null;
    private List<AppInfomation> mlistAppInfo = null;
    public static final int FILTER_ALL_APP = 0; // 所有应用程序
    public static final int FILTER_SYSTEM_APP = 1; // 系统程序
    public static final int FILTER_THIRD_APP = 2; // 第三方应用程序
    public static final int FILTER_SDCARD_APP = 3; // 安装在SDCard的应用程序


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appactivity);
        Intent intent=this.getIntent();
        listView = (ListView) findViewById(R.id.app_list);
        //progressBar=(ProgressBar) findViewById(R.id);

        int filter=intent.getIntExtra("type",0);
        mlistAppInfo = queryFilterAppInfo(filter);
        AppBrowserAdapter AppBrowserAdapter = new AppBrowserAdapter(
                this, mlistAppInfo);


        listView.setAdapter(AppBrowserAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String packagename=mlistAppInfo.get(position).getPackagename();
                String appname=mlistAppInfo.get(position).getAppname();
                Drawable appicon=mlistAppInfo.get(position).getAppicon();
                Intent fuzzintent=new Intent(AppBrowserActivity.this,FuzzerActivity.class);
                fuzzintent.putExtra("packagename",packagename);//把点击的包名传送过去
                fuzzintent.putExtra("appname",appname);
                drawableToBitamp(appicon);
                fuzzintent.putExtra("appicon",bitmap);
                startActivity(fuzzintent);
            }
        });
    }
    private List<AppInfomation> queryFilterAppInfo(int filter) {
        pm = this.getPackageManager();
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> listAppcations = pm
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations,
                new ApplicationInfo.DisplayNameComparator(pm));// 排序
        List<AppInfomation> appInfos = new ArrayList<AppInfomation>(); // 保存过滤查到的AppInfo
        // 根据条件来过滤
        switch (filter) {
            case FILTER_ALL_APP: // 所有程序
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    appInfos.add(getAppInfo(app));
                }
                return appInfos;

            case FILTER_SYSTEM_APP: // 系统应用程序
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        appInfos.add(getAppInfo(app));
                    }
                }
                return appInfos;

            case FILTER_THIRD_APP: // 第三方应用程序
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    //非系统程序
                    if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                        appInfos.add(getAppInfo(app));
                    }
                    //本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
                    else if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
                        appInfos.add(getAppInfo(app));
                    }
                }
                break;

            case FILTER_SDCARD_APP: // 安装在SDCard的应用程序
                appInfos.clear();
                for (ApplicationInfo app : listAppcations) {
                    if ((app.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                        appInfos.add(getAppInfo(app));
                    }
                }
                return appInfos;
            default:
                return null;
        }
        return appInfos;
    }
    // 构造一个AppInfo对象 ，并赋值
    private AppInfomation getAppInfo(ApplicationInfo app) {
        AppInfomation appInfo = new AppInfomation();
        appInfo.setAppname((String) app.loadLabel(pm));
        appInfo.setAppicon(app.loadIcon(pm));
        appInfo.setPackagename(app.packageName);
        try {
            appInfo.setVersionname(pm.getPackageInfo(app.packageName,0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo;
    }

    private void drawableToBitamp(Drawable drawable)
    {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        bitmap = bd.getBitmap();
    }
}
