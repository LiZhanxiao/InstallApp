package com.lizhanxiao.app.installapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.util.List;

public class checkFile extends Service {
    private final String SDPATH  = Environment.getExternalStorageDirectory()+File.separator;
    private final String PATH_FILE = SDPATH+"Download"+File.separator+"apps";
    private final String INSTALLDATE = "installDate";
    private final String appName = "KLXT";
    public checkFile() {

    }

    /**
     * 每1分钟更新一次数据
     */
    private static final int ONE_Miniute=3*1000;
    private static final int PENDING_REQUEST=0;


    /**
     * 调用Service都会执行到该方法
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //这里模拟后台操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (FileUtils.createOrExistsDir(PATH_FILE)) {
                    List<File> files  = FileUtils.listFilesInDir(PATH_FILE);
                    for (File file : files) {
                        if (file.getName().contains(appName)){
                            long newDate = FileUtils.getFileLastModified(file);
                            if (SPUtils.getInstance().contains(INSTALLDATE)) {
                                long oldDate =  SPUtils.getInstance().getLong(INSTALLDATE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    if (Long.compare(oldDate,newDate) !=0) {

                                        AppUtils.installApp(file);
                                        SPUtils.getInstance().put(INSTALLDATE,newDate);
                                    }
                                }
                            }else {
                                AppUtils.installApp(file);
                                SPUtils.getInstance().put(INSTALLDATE,newDate);
                            }
                        }
                    }
                }
            }
        }).start();

        //通过AlarmManager定时启动广播
        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime= SystemClock.elapsedRealtime()+ONE_Miniute;//从开机到现在的毫秒书（手机睡眠(sleep)的时间也包括在内
        Intent i=new Intent(this, AlarmReceive.class);
        PendingIntent pIntent=PendingIntent.getBroadcast(this,PENDING_REQUEST,i,PENDING_REQUEST);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
