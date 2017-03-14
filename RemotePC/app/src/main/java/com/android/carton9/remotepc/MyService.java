package com.android.carton9.remotepc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class MyService extends Service {
    WebData sender=null;
    public MyService() {
    }
    String buff;
    WebDataInterface.Stub mBinder=new WebDataInterface.Stub() {
        @Override
        public void getData(String data) throws RemoteException {
            buff=data;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sender.send(buff.getBytes());
                }
            }).start();
        }
    };
    public void onCreate() {

        sender =new WebData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sender.init();
            }
        }).start();
        //sender.send("good".getBytes());
    }
    public void onDestroy(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                sender.stop();
            }
        }).start();
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
}
