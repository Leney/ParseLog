package com.xd.parselog.util;

import android.os.Environment;
import android.util.Log;

import com.google.common.collect.Lists;
import com.xd.parselog.db.DeviceDBManager;
import com.xd.parselog.model.DeviceInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by llj on 2017/8/4.
 */

public class Tools {
    /**
     * 通过BufferedRandomAccessFile读取文件,并插入到数据库中去
     *
     * @param file     源文件
     */
    public static void bufferedRandomAccessFileReadLineAndInsertToDB(File file) {
//        Map<String, Object> res = Maps.newHashMap();
        List<DeviceInfo> deviceInfos = Lists.newArrayList();
        List<String> tempImeis = Lists.newArrayList();
//        res.put("pins", deviceInfos);
        BufferedRandomAccessFile reader = null;
        try {
            reader = new BufferedRandomAccessFile(file, "r");
//            reader.seek(pos);

            String pin;
            while ((pin = reader.readLine()) != null) {
                Log.i("llj", "pin------>>>" + pin);
                if (StringUtils.isBlank(pin)) {
                    break;
                }
                if(isContainsInvalid(pin)){
                    // 如果包含无效字符，放弃，进入下一次循环
                    continue;
                }

                DeviceInfo deviceInfo = getDeviceInfoByRead(pin);
                if (deviceInfo == null) continue;

                if (!tempImeis.contains(deviceInfo.imei)) {
                    // 如果之前没有此条数据，则加入到集合中去
                    tempImeis.add(deviceInfo.imei);
                    deviceInfos.add(deviceInfo);
                }
//                deviceInfos.add(getDeviceInfoByRead(line));

                if (deviceInfos.size() >= 10000) {
                    // 每10000条数据开始往数据库中写
                    Log.i("llj", "有10000条了开始插入数据到数据库中去");
                    // 批量插入到数据库中去
                    DeviceDBManager.getInstance().insertListDataBySql(deviceInfos);
                    deviceInfos.clear();
                    tempImeis.clear();
                }
            }

//            for (int i = 0; i < num; i++) {
//                String pin = reader.readLine();
//                if (StringUtils.isBlank(pin)) {
//                    break;
//                }
//                String line = new String(pin.getBytes("8859_1"), encoding);
//                Log.i("llj", "line---"+i+"----->>>>" + line);
//                DeviceInfo deviceInfo = getDeviceInfoByRead(line);
//                if(deviceInfo == null) continue;
//
//                if(!tempImeis.contains(deviceInfo.imei)){
//                    // 如果之前没有此条数据，则加入到集合中去
//                    tempImeis.add(deviceInfo.imei);
//                    deviceInfos.add(deviceInfo);
//                }
////                deviceInfos.add(getDeviceInfoByRead(line));
//
//                if (deviceInfos.size() >= 10000) {
//                    // 每10000条数据开始往数据库中写
//                    Log.i("llj", "有10000条了开始插入数据到数据库中去");
//                    // 批量插入到数据库中去
//                    DeviceDBManager.getInstance().insertListDataBySql(deviceInfos);
//                    deviceInfos.clear();
//                    tempImeis.clear();
//                }
//            }


//            res.put("pos", reader.getFilePointer());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
        }

        Log.i("llj", "读取文件完成！！");
        if (!deviceInfos.isEmpty()) {
            Log.i("llj", "开始插入数据到数据库中去");
            // 批量插入到数据库中去
            DeviceDBManager.getInstance().insertListDataBySql(deviceInfos);
        }
//        return res;
    }

    /**
     * 判断是否包含无效字符
     * @param resource
     * @return
     */
    private static boolean isContainsInvalid(String resource){
        return resource.contains("browser")||resource.contains("Browser")||resource.contains("LieBaoFast")
                || resource.contains("baidu") || resource.contains("SohuNews") || resource.contains("MagicKitchen");
    }


    /**
     * 根据从文件中读取出来的一行数据得到deviceInfo信息
     * 可以在这里设置一些默认值、随机值等
     * ="imei_start:"&A1&"imei_end;"
     * ="android_id_start:"&B1&"android_id_end;"
     *
     * @param source
     * @return
     */
    private static DeviceInfo getDeviceInfoByRead(String source) {
        DeviceInfo deviceInfo;
        try {
            JSONObject resultObject = new JSONObject(subJson(source, "request kdxf  data  "));
            deviceInfo = new DeviceInfo();
            deviceInfo.orientation = resultObject.getInt("orientation");
            deviceInfo.osv = resultObject.getString("osv");
            deviceInfo.operator = resultObject.getString("operator");
            deviceInfo.is_support_deeplink = resultObject.getInt("is_support_deeplink");
            deviceInfo.density = resultObject.getString("density");
            deviceInfo.net = resultObject.getInt("net");
            deviceInfo.lan = resultObject.getString("lan");
            deviceInfo.vendor = resultObject.getString("vendor");
            deviceInfo.mac = resultObject.getString("mac");
            //TODO android id 有问题
//            deviceInfo.adid = resultObject.getString("adid");
            deviceInfo.batch_cnt = resultObject.getString("batch_cnt");
            deviceInfo.imei = resultObject.getString("imei");
            deviceInfo.adw = resultObject.getInt("adw");
            deviceInfo.adh = resultObject.getInt("adh");
            deviceInfo.dvw = resultObject.getInt("dvw");
            deviceInfo.dvh = resultObject.getInt("dvh");
            deviceInfo.os = resultObject.getString("os");
            deviceInfo.tramaterialtype = resultObject.getString("tramaterialtype");
            deviceInfo.devicetype = resultObject.getString("devicetype");
            deviceInfo.model = resultObject.getString("model");
            deviceInfo.ua = resultObject.getString("ua");
        } catch (JSONException e) {
            e.printStackTrace();
            deviceInfo = null;
        }
//        deviceInfo.imei = getBettweenStr(source, "imei_start:", "imei_end;");
//        deviceInfo.imsi = "";
//        deviceInfo.androidId = getBettweenStr(source, "android_id_start:", "android_id_end;");
//        deviceInfo.mac = getBettweenStr(source, "mac_start:", "mac_end;");
////        deviceInfo.ip = "";
//        deviceInfo.model = getBettweenStr(source, "model_start:", "model_end;");
//        deviceInfo.vendor = getBettweenStr(source, "vendor_start:", "vendor_end;");
//
//
//        deviceInfo.osVersion = "";
//        deviceInfo.density = "";
//        deviceInfo.operator = "";
//        deviceInfo.userAgent = "";
////        deviceInfo.adWidth = "";
////        deviceInfo.adHeight = "";
//        deviceInfo.deviceScreenWidth = 0;
//        deviceInfo.deviceScreenHeight = 0;
//
//
//        // 设定死都为wifi网络
//        deviceInfo.net = 2;
//        // 设定死都为竖屏
//        deviceInfo.orientation = 0;
//        // 设定死都为中文语言
//        deviceInfo.language = "zh-CN";

        return deviceInfo;
    }


    private static String subJson(String target, String begin) {
        int beginLength = begin.length();
        int length = target.length();
        int indexOf = target.indexOf(begin);
        return target.substring(indexOf + beginLength, length);
    }



    /**
     * 复制文件到sdcard
     * @param oldFile
     */
    public static void copyFile(String oldFile) {
        File f = new File(oldFile); //比如  "/data/data/com.hello/databases/test.db"

        String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        File o = new File(sdcardPath + "/copy.db"); //sdcard上的目标地址

        if (f.exists()) {

            FileChannel outF;

            try {

                outF = new FileOutputStream(o).getChannel();
                Log.i("llj","开始复制！！！");
                new FileInputStream(f).getChannel().transferTo(0, f.length(), outF);

            } catch (FileNotFoundException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            Log.i("llj","复制成功！！！");
        }else {
            Log.i("llj","需要复制的文件不存在！！！");
        }
    }
}
