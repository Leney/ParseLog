package com.xd.parselog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

import com.xd.parselog.model.DeviceInfo;

import java.util.List;


/**
 * Created by dell on 2017/5/13.
 * 设备数据库操作管理类
 */
public class DeviceDBManager {

    private static volatile DeviceDBManager instance = null;
    private DBHelp dbHelp;

    private DeviceDBManager() {
    }


    public void init(Context context) {
        dbHelp = new DBHelp(context.getApplicationContext());
    }

    public static DeviceDBManager getInstance() {
        if (instance == null) {
            synchronized (DeviceDBManager.class) {
                if (instance == null) {
                    instance = new DeviceDBManager();
                }
            }
        }
        return instance;
    }

    /**
     * 开启事务，大批量插入数据
     *
     * @param list 需要插入的集合数据
     * @return
     */
    public boolean insertListDataBySql(List<DeviceInfo> list) {
        if (list.isEmpty()) {
            return false;
        }
        SQLiteDatabase db = null;
        try {
            db = dbHelp.getWritableDatabase();
            String sql = "insert into " + DBHelp.Columns.TABLE_NAME + "("
                    + DBHelp.Columns.ORIENTATION + ","
                    + DBHelp.Columns.OSV + ","
                    + DBHelp.Columns.OPERATOR + ","
                    + DBHelp.Columns.IS_SUPPORT_DEEPLINK + ","
                    + DBHelp.Columns.DENSITY + ","
                    + DBHelp.Columns.NET + ","
                    + DBHelp.Columns.LAN+ ","
                    + DBHelp.Columns.VENDOR + ","
                    + DBHelp.Columns.MAC + ","
                    + DBHelp.Columns.ADID + ","
                    + DBHelp.Columns.BATCH_CNT + ","
                    + DBHelp.Columns.IMEI + ","
                    + DBHelp.Columns.DVW + ","
                    + DBHelp.Columns.DVH + ","
                    + DBHelp.Columns.ADW + ","
                    + DBHelp.Columns.ADH + ","
                    + DBHelp.Columns.OS + ","
                    + DBHelp.Columns.TRAMATERIALTYPE + ","
                    + DBHelp.Columns.DEVICETYPE + ","
                    + DBHelp.Columns.MODEL + ","
                    + DBHelp.Columns.UA
                    + ") " + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

//            String sql = "insert into " + DBHelp.Columns.TABLE_NAME + "("
//                    + DBHelp.Columns.IMEI + ","
//                    + DBHelp.Columns.ANDROID_ID + ") " + "values(?,?)";

            SQLiteStatement stat = db.compileStatement(sql);
            db.beginTransaction();
            for (DeviceInfo deviceInfo : list) {
                stat.bindLong(1, deviceInfo.orientation);
                stat.bindString(2, deviceInfo.osv);
                stat.bindString(3, deviceInfo.operator);
                stat.bindLong(4, deviceInfo.is_support_deeplink);
                stat.bindString(5, deviceInfo.density);
                stat.bindLong(6, deviceInfo.net);
                stat.bindString(7, deviceInfo.lan);
                stat.bindString(8, deviceInfo.vendor);
                stat.bindString(9, deviceInfo.mac);
                stat.bindString(10, deviceInfo.adid);
                stat.bindString(11, deviceInfo.batch_cnt);
                stat.bindString(12, deviceInfo.imei);
                stat.bindLong(13, deviceInfo.dvw);
                stat.bindLong(14, deviceInfo.dvh);
                stat.bindLong(15, deviceInfo.adw);
                stat.bindLong(16, deviceInfo.adh);
                stat.bindString(17, deviceInfo.os);
                stat.bindString(18, deviceInfo.tramaterialtype);
                stat.bindString(19, deviceInfo.devicetype);
                stat.bindString(20, deviceInfo.model);
                stat.bindString(21, deviceInfo.ua);
                long result = stat.executeInsert();
                if (result < 0) {
                    return false;
                }
            }
            db.setTransactionSuccessful();
            Log.i("llj", "插入到数据库中完成！！！！");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (null != db) {
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 更新软件管理表中的某条数据
     *
     * @param deviceInfo
     * @return
     */
    public int update(DeviceInfo deviceInfo,int index)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        int count = db.update(DBHelp.Columns.TABLE_NAME, getContentValues(deviceInfo), BaseColumns._ID + "= " + index,null);
        Log.i("llj","更新数据是否成功？？？count----->>"+count);
        db.close();
        return count;
    }

    /**
     * 根据id查找数据库中的一条Device数据
     *
     * @return
     */
    public DeviceInfo queryDeviceInfo(int id) {
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        DeviceInfo deviceInfo = null;
        Cursor cursor = db.query(DBHelp.Columns.TABLE_NAME, null,
                DBHelp.Columns._ID + "=" + id, null, null, null, null);
        while (cursor.moveToNext()) {
            deviceInfo = getDeviceInfo(cursor);
        }
        db.close();
        return deviceInfo;
    }


    /**
     * 随机查询一条数据
     *
     * @return
     */
    public DeviceInfo queryDeviceInfoByRandom() {
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        DeviceInfo deviceInfo = null;
        Cursor cursor = db.query(DBHelp.Columns.TABLE_NAME, null, null, null, null, null, "RANDOM()", "1");
        while (cursor.moveToNext()) {
            deviceInfo = getDeviceInfo(cursor);
        }
        cursor.close();
        db.close();
        return deviceInfo;
    }


//    public void getAllData() {
//        String sql = "Select " + BaseColumns._ID + " from " + DBHelp.Columns.TABLE_NAME + ";";
//        SQLiteDatabase db = dbHelp.getReadableDatabase();
//        Cursor cursor = db.rawQuery(sql,null);
//        if(cursor.moveToNext()){
//            Log.i("llj","第一条_id的值为-------->>>"+cursor.getInt(0));
//        }
//    }

    /**
     * 获取当前数据库中的数据总条数
     *
     * @return
     */
    public int getDataCount() {
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        String sql = "Select count(" + BaseColumns._ID + ") from " + DBHelp.Columns.TABLE_NAME + ";";
//        String sql = "SELECT * FROM "+ DBHelp.Columns.TABLE_NAME+" ORDER BY "+ BaseColumns._ID+" DESC LIMIT 1";
        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    /**
     * 从查询结果中获取出一条数据
     *
     * @param cursor
     * @return 一条数据 DeviceInfo
     * <p>
     * + DBHelp.Columns.LANGUAGE
     */
    private DeviceInfo getDeviceInfo(Cursor cursor) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.orientation = cursor.getInt(cursor.getColumnIndex(DBHelp.Columns.ORIENTATION));
        deviceInfo.osv = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.OSV));
        deviceInfo.operator = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.OPERATOR));
        deviceInfo.is_support_deeplink = cursor.getInt(cursor.getColumnIndex(DBHelp.Columns.IS_SUPPORT_DEEPLINK));
        deviceInfo.density = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.DENSITY));
        deviceInfo.net = cursor.getInt(cursor.getColumnIndex(DBHelp.Columns.NET));
        deviceInfo.lan = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.LAN));
        deviceInfo.vendor = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.VENDOR));
        deviceInfo.mac = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.MAC));
        deviceInfo.adid = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.ADID));
        deviceInfo.batch_cnt = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.BATCH_CNT));
        deviceInfo.imei = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.IMEI));
        deviceInfo.dvw = cursor.getInt(cursor.getColumnIndex(DBHelp.Columns.DVW));
        deviceInfo.dvh = cursor.getInt(cursor.getColumnIndex(DBHelp.Columns.DVH));
        deviceInfo.adw = cursor.getInt(cursor.getColumnIndex(DBHelp.Columns.ADW));
        deviceInfo.adh = cursor.getInt(cursor.getColumnIndex(DBHelp.Columns.ADH));
        deviceInfo.os = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.OS));
        deviceInfo.tramaterialtype = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.TRAMATERIALTYPE));
        deviceInfo.devicetype = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.DEVICETYPE));
        deviceInfo.model = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.MODEL));
        deviceInfo.ua = cursor.getString(cursor.getColumnIndex(DBHelp.Columns.UA));
        return deviceInfo;
    }

    /**
     * 将AppInfo对象转换成ContentValues对象
     * @param deviceInfo
     * @return
     */
    private ContentValues getContentValues(DeviceInfo deviceInfo)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelp.Columns.ORIENTATION,deviceInfo.orientation);
        values.put(DBHelp.Columns.OSV,deviceInfo.osv);
        values.put(DBHelp.Columns.OPERATOR,deviceInfo.operator);
        values.put(DBHelp.Columns.IS_SUPPORT_DEEPLINK,deviceInfo.is_support_deeplink);
        values.put(DBHelp.Columns.DENSITY,deviceInfo.density);
        values.put(DBHelp.Columns.NET,deviceInfo.net);
        values.put(DBHelp.Columns.LAN,deviceInfo.lan);
        values.put(DBHelp.Columns.VENDOR,deviceInfo.vendor);
        values.put(DBHelp.Columns.MAC,deviceInfo.mac);
        values.put(DBHelp.Columns.ADID,deviceInfo.adid);
        values.put(DBHelp.Columns.BATCH_CNT,deviceInfo.batch_cnt);
        values.put(DBHelp.Columns.IMEI,deviceInfo.imei);
        values.put(DBHelp.Columns.DVW,deviceInfo.dvw);
        values.put(DBHelp.Columns.DVH,deviceInfo.dvh);
        values.put(DBHelp.Columns.ADW,deviceInfo.adw);
        values.put(DBHelp.Columns.ADH,deviceInfo.adh);
        values.put(DBHelp.Columns.OS,deviceInfo.os);
        values.put(DBHelp.Columns.TRAMATERIALTYPE,deviceInfo.tramaterialtype);
        values.put(DBHelp.Columns.DEVICETYPE,deviceInfo.devicetype);
        values.put(DBHelp.Columns.MODEL,deviceInfo.model);
        values.put(DBHelp.Columns.UA,deviceInfo.ua);
        return values;
    }
}
