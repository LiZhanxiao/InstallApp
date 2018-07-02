package com.lizhanxiao.app.installapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.util.List;

public class MainActivity extends Activity {

    private Button startBtn;
    private Button stopBtn;
    private Intent mIntent;
    private final String SDPATH  = Environment.getExternalStorageDirectory()+File.separator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIntent = new Intent(this, checkFile.class);
        startBtn = (Button) findViewById(R.id.main_start);
        stopBtn = (Button) findViewById(R.id.main_stop);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
    }

    private void stop() {
        if (mIntent != null) {
            stopService(mIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void start() {
        if (mIntent != null) {
            startService(mIntent);
        }
    }

}
