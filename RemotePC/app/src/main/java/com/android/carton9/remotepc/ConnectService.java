package com.android.carton9.remotepc;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by qazwq on 2017/2/22.
 */

public class ConnectService {
    private final String serviceName=MyService.class.getName();
    Intent start;
    private WebDataInterface service=null;
    private ServiceConnection mServiceConnection;
    Context luncher;
    public ConnectService(Context luncher){
        this.luncher=luncher;
        start=new Intent(luncher,MyService.class);
        mServiceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                service=WebDataInterface.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                service=null;
            }
        };
        luncher.startService(start);
    }
    public void connect(){
        Intent intent = new Intent(WebDataInterface.class.getName());
        intent.setClassName("com.android.carton9.remotepc",serviceName);
        luncher.bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }
    public void disconnect(){
        Intent intent = new Intent(WebDataInterface.class.getName());
        luncher.unbindService(mServiceConnection);
    }
    public void stop(){
        luncher.stopService(start);
    }
    public boolean isConnect(){
        if(service==null)return false;
        return true;
    }
    public boolean isWork(String name){
        ActivityManager mActivityManager=(ActivityManager)luncher.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> s= mActivityManager.getRunningServices(500);
        for (int i = 0; i < s.size(); i++) {
            if (s.get(i).service.getClassName().toString().equals(serviceName)) return true;
        }
        return false;
    }
    public WebDataInterface getService() {
        return service;
    }
}
