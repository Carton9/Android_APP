package com.android.carton9.pickup;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
/*
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="android.permission.READ_PROFILE" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
 */
public class LocationCheck extends Service {
    public LocationCheck() {
    }
    double distance;
    double newDistance;
    String phoneNumber="";
    String text="";
    LocationCheck self = this;
    public double ln;
    double la;
    boolean isLive = false;
    NotificationManager mNotificationManager;
    LocationManager locationManager;
    NotificationCompat.Builder mBuilder;
    final int iD = 9982;
    LocationInterface.Stub mBinder =new LocationInterface.Stub() {
        @Override
        public void sendInfo(double _ln, double _la, String _phoneNumber, String _text) throws RemoteException {
            ln=_ln;
            la=_la;
            phoneNumber=_phoneNumber;
            text=_text;
            isLive=true;
        }

        @Override
        public void action() throws RemoteException {
            if(isLive){
                if (ActivityCompat.checkSelfPermission(self, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(self, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    sendText("获取权限失败",true);
                    stopSelf();
                    return;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                sendText("程序正在运行",true);
                if (location != null) act(location);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 5, locationListener);
            }
        }
    };
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 当GPS定位信息发生改变时，更新位置
            act(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        public void onProviderEnabled(String provider) {
            if (ActivityCompat.checkSelfPermission(self, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(self, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            act(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onProviderDisabled(String s) {
            sendText("无法检测到GPS",true);
        }
    };
    @Override
    public void onCreate() {
        //la=34.0227146;
        //ln=-117.799577;
        //la = 33.979632;
        //ln = -117.900684;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("接送通知器")
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
                .setTicker("2333333333")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(true)
                .setProgress(0,100,true)
                .setSmallIcon(R.drawable.ic_notifation);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
    @Override
    public void onDestroy() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            sendText("获取权限失败",true);
            return;
        }
        locationManager.removeUpdates(locationListener);
        mBuilder.setProgress(0,0,false);
        sendText("感谢您使用本程序",true);
        delay(1000);
        delay(1000);
        mNotificationManager.cancel(iD);
        //super.onDestroy();
    }
    public void delay(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }
    public void sendText(String text,boolean isEnd){
        if(isEnd){
            mBuilder.setContentText(text)
                    .setOngoing(false);
            mNotificationManager.notify(iD, mBuilder.build());
            delay(1000);
        }
        else {

            mBuilder.setProgress((int)distance,(int)newDistance,false);
            mBuilder.setContentText(text);
            mNotificationManager.notify(iD, mBuilder.build());
            delay(100);
        }
    }
    public void sendMessege(){
        SmsManager smsm = SmsManager.getDefault();
        try {
            if(phoneNumber.equals("")||text.equals("")){
                sendText("你没有设置号码或消息内容",true);
                return;
            }
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(), 0);
            smsm.sendTextMessage(phoneNumber, null, text, null, null);
        } catch (Exception e) {
            Log.e("SmsSending", "SendException", e);
        }
    }
    public void act(Location location){
        if(isLive){
            if(MapMath.GetDistance(location.getLongitude(),location.getLatitude(),ln,la)<1000){
                sendText("您将要到达学校",true);
                sendMessege();
                stopSelf();
            }
            else{
                newDistance=MapMath.GetDistance(location.getLongitude(),location.getLatitude(),ln,la);
                sendText("您现在距离学校"+newDistance+"米",false);
            }
        }
        else{
            distance=MapMath.GetDistance(location.getLongitude(),location.getLatitude(),ln,la);
        }

    }
    public PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }
}
