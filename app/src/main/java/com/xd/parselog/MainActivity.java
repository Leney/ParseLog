package com.xd.parselog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xd.parselog.db.DeviceDBManager;
import com.xd.parselog.util.Tools;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import static javax.xml.transform.OutputKeys.ENCODING;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getDeviceData(File file) {
        long pos = 0L;
        while (true) {
            Map<String, Object> res = Tools.bufferedRandomAccessFileReadLineAndInsertToDB(file, ENCODING, pos);
            // 如果返回结果为空结束循环
            if (MapUtils.isEmpty(res)) {
                break;
            }
            List<String> pins = (List<String>) res.get("pins");
            if (CollectionUtils.isNotEmpty(pins)) {
//                logger.info(Arrays.toString(pins.toArray()));
                if (pins.size() < NUM) {
                    break;
                }
            } else {
                break;
            }
            pos = (Long) res.get("pos");
        }

        Log.i("llj","数据库中的数据总条数-------》》》"+ DeviceDBManager.getInstance().getDataCount());
    }


}
