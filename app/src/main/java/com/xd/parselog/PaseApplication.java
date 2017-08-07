package com.xd.parselog;

import android.app.Application;

import com.xd.parselog.db.DeviceDBManager;

/**
 * Created by llj on 2017/8/7.
 */
public class PaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        DeviceDBManager.getInstance().init(getApplicationContext());
    }
}
