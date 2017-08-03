package com.xd.parselog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DBHelp extends SQLiteOpenHelper {

    private static final String TAG = "DBHelp";

    // 数据库名称
    private static final String DATABASE_NAME = "DeviceInfoDb";

    // 数据库版本
    private static final int DATABASE_VERSION = 1;

    /**
     * 创建软件管理表的sql语句
     */
    private static final String CREATE_SOFTWARES_TABLE = "create table if not exists "
            + Columns.TABLE_NAME
            + " ("
            + Columns._ID
            + " integer primary key autoincrement, "
            + Columns.ORIENTATION
            + " integer, "
            + Columns.OSV
            + " text, "
            + Columns.OPERATOR
            + " text, "
            + Columns.IS_SUPPORT_DEEPLINK
            + " integer,"
            + Columns.DENSITY
            + " text,"
            + Columns.NET
            + " integer,"
            + Columns.LAN
            + " text,"
            + Columns.VENDOR
            + " text,"
            + Columns.MAC
            + " text,"
            + Columns.ADID
            + " text,"
            + Columns.BATCH_CNT
            + " text,"
            + Columns.IMEI
            + " text,"
            + Columns.DVW
            + " integer,"
            + Columns.DVH
            + " integer,"
            + Columns.ADW
            + " integer,"
            + Columns.ADH
            + " integer,"
            + Columns.OS
            + " text,"
            + Columns.TRAMATERIALTYPE
            + " text,"
            + Columns.DEVICETYPE
            + " text,"
            + Columns.MODEL
            + " text,"
            + Columns.UA
            + " text);";


//    /**
//     * 创建软件管理表的sql语句
//     */
//    private static final String CREATE_SOFTWARES_TABLE = "create table if not exists "
//            + Columns.TABLE_NAME
//            + " ("
//            + Columns._ID
//            + " integer primary key autoincrement, "
//            + Columns.IMEI
//            + " text, "
//            + Columns.IMSI
//            + " text, "
//            + Columns.ANDROID_ID
//            + " text, "
//            + Columns.MAC_ADDRESS
////            + " text,"
////            + Columns.IP_ADDRESS
//            + " text,"
//            + Columns.MODEL
//            + " text,"
//            + Columns.OS_VERSION
//            + " text,"
//            + Columns.DENSITY
//            + " text,"
//            + Columns.OPERATOR
//            + " text,"
//            + Columns.USER_AGENT
//            + " text,"
//            + Columns.NET
//            + " integer,"
//            + Columns.ORIENTATION
//            + " integer,"
//            + Columns.LANGUAGE
//            + " text,"
////			 + Columns.SAME_SIGN
////			 + " bit,"
////			 + Columns.IS_INLAY
////			 + " bit,"
////			 + Columns.IS_SHOW_INSTALLED_LIST
////			 + " bit,"
////            + Columns.AD_WIDTH
////            + " text,"
////            + Columns.AD_HEIGHT
////            + " text,"
//            + Columns.DEVICE_SCREEN_WIDTH
//            + " integer,"
//            + Columns.DEVICE_SCREEN_HEIGHT
//            + " integer,"
//            + Columns.VENDOR
//            + " text);";

//    /**
//     * 创建软件管理表的sql语句
//     */
//    private static final String CREATE_SOFTWARES_TABLE = "create table if not exists "
//            + Columns.TABLE_NAME
//            + " ("
//            + Columns._ID
//            + " integer primary key autoincrement, "
//            + Columns.IMEI
//            + " text, "
//            + Columns.ANDROID_ID
//            + " text);";

    /**
     * 删除软件管理表的sql语句
     */
    private static final String DELETE_SOFTWARE_TABLE = "drop table "
            + Columns.TABLE_NAME;

    // /** 创建更新软件数量的表 */
    // private static final String CREATE_UPDATE_COUNT_TABLE =
    // "create table if not exists "
    // + "miss_infos "
    // + "("
    // + Columns._ID
    // + " integer primary key autoincrement," + " miss integer);";

//    /**
//     * 删除更新软件数量的表sql语句
//     */
//    private static final String DELETE_UPDATE_COUNT_TABLE = "drop table miss_infos;";

    public DBHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SOFTWARES_TABLE);
        // db.execSQL(CREATE_UPDATE_COUNT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_SOFTWARE_TABLE);
        db.execSQL(CREATE_SOFTWARES_TABLE);

//        try {
//            db.execSQL(DELETE_UPDATE_COUNT_TABLE);
//        } catch (Exception e) {
//            Log.i("llj", "要删除的表不存在！！");
//            Log.e(TAG, "onUpgrade()#exception", e);
//        }
        // DLog.i("lilijun", "创建了更新数量表！！");
        // db.execSQL(CREATE_UPDATE_COUNT_TABLE);

        Log.i(TAG, "oldVersion=" + oldVersion + ", newVersion=" + newVersion);
    }

    public static class Columns implements BaseColumns {
        /**
         * 表名称
         */
        public static final String TABLE_NAME = "device_tab";

        /**
         * 横竖屏 0=竖屏，1=横屏
         */
        public static final String ORIENTATION = "orientation";
        /**
         * 操作系统版本号
         */
        public static final String OSV = "osv";
        /**
         * 网络运营商
         */
        public static final String OPERATOR = "operator";
        /**
         * 是否支持跳转到其它应用 0=不支持，1=支持
         */
        public static final String IS_SUPPORT_DEEPLINK = "is_support_deeplink";
        /**
         * 屏幕密度
         */
        public static final String DENSITY = "density";
        /**
         * 联网类型(0—未知，1—Ethernet， 2—wifi，3—蜂窝网络，未知代， 4—， 2G， 5—蜂窝网络， 3G， 6—蜂窝网络，4G)
         */
        public static final String NET = "net";
        /**
         * 使用语言
         */
        public static final String LAN = "lan";
        /**
         * 设备生产商名称
         */
        public static final String VENDOR = "vendor";
        /**
         * mac 地址
         */
        public static final String MAC = "mac";
        /**
         * android id
         */
        public static final String ADID = "adid";
        /**
         * 请求批量下发广告的数量,目前只能为”1”
         */
        public static final String BATCH_CNT = "batch_cnt";
        /**
         * imei
         */
        public static final String IMEI = "imei";

        /**
         * 设备屏幕的宽度，以像素为单位
         */
        public static final String DVW = "dvw";
        /**
         * 设备屏幕的高度，以像素为单位
         */
        public static final String DVH = "dvh";
        /**
         * 广告位的宽度，以像素为单位
         */
        public static final String ADW = "adw";
        /**
         * 广告位的高度，以像素为单位
         */
        public static final String ADH = "adh";
        /**
         * 客户端操作系统的类型 值有：Android、iOS、WP(注意大小写)
         */
        public static final String OS = "os";
        /**
         * 数据类型 html、json、htmlorjson(既支持json 又 支持html)
         */
        public static final String TRAMATERIALTYPE = "tramaterialtype";
        /**
         * 设备类型 -1-未知，0 - phone，1 - pad，2 - pc，3 - tv，4 - wap
         */
        public static final String DEVICETYPE = "devicetype";
        /**
         * 机型
         */
        public static final String MODEL = "model";
        /**
         * user_agent
         */
        public static final String UA = "ua";

    }

}
