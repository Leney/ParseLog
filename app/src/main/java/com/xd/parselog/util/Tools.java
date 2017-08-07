package com.xd.parselog.util;

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
import java.util.Random;

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

                if(tempImeis.contains(deviceInfo.imei)) continue;

//                if (!tempImeis.contains(deviceInfo.imei)) {
                    // 如果之前没有此条数据，则加入到集合中去

                tempImeis.add(deviceInfo.imei);
                deviceInfo.adid = getAndroidId();
                deviceInfo.ua = getRandomUserAgent(deviceInfo);
                deviceInfos.add(deviceInfo);
//                }
//                deviceInfos.add(getDeviceInfoByRead(line));

                int size = deviceInfos.size();
                Log.i("test","androidId------------>>"+deviceInfo.adid);
                Log.i("test","ua------------>>"+deviceInfo.ua);
                Log.e("llj","已经插入 "+size+" 条数据！！");
                if (size >= 10000) {
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
            JSONObject resultObject = new JSONObject(subJson(source, "client request data:"));
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
//            deviceInfo.adid = getAndroidId();
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
//            deviceInfo.ua = resultObject.getString("ua");
//            deviceInfo.ua = getRandomUserAgent(deviceInfo);
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

//        String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String sdcardPath = "/mnt/shared/Other";

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


    /**
     * 根据deviceInfo 生成userAgent
     * @param deviceInfo
     * @return
     *
     * Mozilla/5.0 ((Linux; U; Android 4.4.4; Lenovo A2800-d Build/KTU84P)) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 OPR/12.7.0.4 Mobile Safari/537.36
     * Mozilla/5.0 (Linux; U; Android 3.0; en-us; Xoom Build/HRI39) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13
     * Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; Nexus One Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1
     * Mozilla/5.0 (Linux; android 4.4.4; SAMSUNG-SM-N900A Build/tt) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36
     * Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1
     * Mozilla/5.0 (Linux; U; Android 4.3; zh-cn; Coolpad 8720L Build/JSS15Q) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30
     * Mozilla/5.0 (Linux; U; Android 4.1.2; zh-cn; SCH-I759 Build/JZO54K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30
     *
     * 48e62c2f9d71ff91
     */
    public static String getRandomUserAgent(DeviceInfo deviceInfo){
        StringBuilder builder = new StringBuilder("Mozilla/5.0 (Linux; U; Android ");
        builder.append(deviceInfo.osv);
        builder.append("; zh-cn; ");
        builder.append(deviceInfo.model);
        builder.append("Build/");
        builder.append(getRandomStr(5));
        builder.append(") AppleWebKit/");
        String floatStr = getRamdonFloat();
        builder.append(floatStr);
        builder.append(" (KHTML, like Gecko) Version/4.0 Mobile Safari/");
        builder.append(floatStr);
        return builder.toString();
    }

    /**
     * 随机生成Android id
     */
    public static String getAndroidId() {
        String str = "";
        for (int i = 0; i < 16; i++) {
            char temp = 0;
            int key = (int) (Math.random() * 2);
            switch (key) {
                case 0:
                    temp = (char) (Math.random() * 10 + 48);//产生随机数字
                    break;
                case 1:
                    temp = (char) (Math.random()*6 + 'a');//产生a-f
                    break;
                default:
                    break;
            }
            str = str + temp;
        }
        return str;
    }

    /**
     * 随机获取多少位的字符串(大写)
     * @param size
     * @return
     */
    private static String getRandomStr(int size){
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 随机获取固定的几个float类型的字符串
     * @return
     */
    private static String getRamdonFloat(){
        Random random = new Random();
        int number = random.nextInt(4);
        if(number == 0){
            return "534.30";
        }else if (number == 1){
            return "533.1";
        }else {
            return "537.36";
        }
    }
}
