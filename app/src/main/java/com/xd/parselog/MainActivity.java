package com.xd.parselog;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xd.parselog.util.Tools;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button parseBtn = (Button) findViewById(R.id.parse_btn);
        parseBtn.setOnClickListener(this);

        Button exportBtn = (Button) findViewById(R.id.export_db_btn);
        exportBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.parse_btn:
                // 解析
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String path = Environment.getExternalStorageDirectory().getPath()+"/install.txt";
                        File file = new File(path);

                        Tools.bufferedRandomAccessFileReadLineAndInsertToDB(file);
                    }
                }).start();
                break;
            case R.id.export_db_btn:
                // 导出数据库文件

//                String path = Environment.getExternalStorageDirectory().getPath();
//                Log.i("llj","path-------->>>"+path);
//                File file = new File(path+"/log.txt");\
//                Log.i("llj","file.exists()-------->>>"+file.exists());

                Tools.copyFile("/data/data/com.xd.parselog/databases/DeviceInfoDb");
                break;
        }
    }

//    public void getSDPath() {
//        File sdDir = null;
//        File sdDir1 = null;
//        File sdDir2 = null;
//        boolean sdCardExist = Environment.getExternalStorageState()
//                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
//        if (sdCardExist) {
//            sdDir = getExternalStorageDirectory();//获取跟目录
//
//            sdDir1 = Environment.getDataDirectory();
//            sdDir2 = Environment.getRootDirectory();
//        }
//        System.out.println("getExternalStorageDirectory(): " + sdDir.getAbsolutePath());
//        System.out.println("getDataDirectory(): " + sdDir1.getAbsolutePath());
//        System.out.println("getRootDirectory(): " + sdDir2.getAbsolutePath());
//    }


}
