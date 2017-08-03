package com.xd.parselog.model;

/**
 * Created by dell on 2017/5/13.
 * 广告请求所需的设备信息model
 */
public class DeviceInfo {

    /** 横竖屏 0=竖屏，1=横屏*/
    public int orientation;
    /** 操作系统版本号*/
    public String osv;
    /** 网络运营商*/
    public String operator;
    /** 是否支持跳转到其它应用 0=不支持，1=支持*/
    public int is_support_deeplink;
    /** 屏幕密度*/
    public String density;
    /** 联网类型(0—未知，1—Ethernet， 2—wifi，3—蜂窝网络，未知代，4—， 2G， 5—蜂窝网络， 3G， 6—蜂窝网络，4G)*/
    public int net;
    /** 使用语言  zh-CN*/
    public String lan;
    /** 设备生产商名称*/
    public String vendor;
    /** mac地址*/
    public String mac;
    /** android id*/
    public String adid;
    /** 请求批量下发广告的数量,目前只能为”1” */
    public String batch_cnt;
    /** imei*/
    public String imei;
    /**设备屏幕的宽度，以像素为单位*/
    public int dvw;
    /**设备屏幕的高度，以像素为单位*/
    public int dvh;
    /** 广告位的宽度，以像素为单位*/
    public int adw;
    /** 广告位的高度，以像素为单位*/
    public int adh;
    /** 客户端操作系统的类型 值有：Android、iOS、WP(注意大小写)*/
    public String os;
    /** 数据类型 html、json、htmlorjson(既支持json 又 支持html)*/
    public String tramaterialtype;
    /** 设备类型 -1-未知，0 - phone，1 - pad，2 - pc，3 - tv，4 - wap*/
    public String devicetype;
    /** 设备型号(机型)*/
    public String model;
    /** user_agent*/
    public String ua;


    /**未加入的讯飞需要的参数：*/
    // ts(请求时的时间戳)
    // ip(请求的ip)
    // adunitid(请求的广告位id)
    // pkgname(应用包名)
    // appname(应用名称)
    // appid(讯飞后台的应用id)
    // isboot(是否为开屏广告 1=开屏，0=非开屏)



    /**武总那边有问题的字段*/
    // adid (android id 的值大部分是imei的值代替的)
    // devicetype(设备类型全是4，4代表的是wap)
}
