package com.example.qazwq.myapplication;

import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.content.Context;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    MainActivity a = this;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Button button=(Button)this.findViewById(R.id.button4);
        Button button2=(Button)this.findViewById(R.id.button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsMgr = SmsManager.getDefault();
                Intent sentIntent = new Intent("com.myself.action.SMS_SEND_RESULT");
                PendingIntent dummySentEvent = PendingIntent.getBroadcast(a, 0, sentIntent, 0);
                Intent deliveryIntent = new Intent("com.myself.action.SMS_DELIVERY_RESULT");
                PendingIntent dummyDeliveryEvent = PendingIntent.getBroadcast(a, 0, deliveryIntent, 0);
                try {
                    smsMgr.sendTextMessage("9097630266", null, "text",dummySentEvent, dummyDeliveryEvent);
                } catch (Exception e) {
                    //Log.e("SmsSending", "SendException", e);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(a, Main2Activity.class);
                startActivity(intent);
            }
        });
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                20, 8, new LocationListener() {

                    @Override
                    public void onLocationChanged(Location location) {
                        // 当GPS定位信息发生改变时，更新位置
                        updateView(location);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        updateView(null);
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        // 当GPS LocationProvider可用时，更新位置
                        updateView(null);

                    }

                    @Override
                    public void onStatusChanged(String provider, int status,
                                                Bundle extras) {
                    }
                });
    }
    public void updateView(Location location){
        if(a==null)return;
        StringBuffer sb = new StringBuffer();
        sb.append("实时的位置信息：\n经度：");
        sb.append(location.getLongitude());
        sb.append("\n纬度：");
        sb.append(location.getLatitude());
        sb.append("\n高度：");
        sb.append(location.getAltitude());
        sb.append("\n速度：");
        sb.append(location.getSpeed());
        sb.append("\n方向：");
        sb.append(location.getBearing());
        sb.append("\n精度：");
        sb.append(location.getAccuracy());
        TextView textView = (TextView)a.findViewById(R.id.textView5);
        CharSequence text=sb.toString();
        textView.setText(text);
    }
}
