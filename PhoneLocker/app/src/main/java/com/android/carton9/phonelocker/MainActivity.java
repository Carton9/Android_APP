package com.android.carton9.phonelocker;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    MainActivity self=this;
    TextView textView;
    WifiManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String a=new String(""+counter.counter);
        textView=(TextView)this.findViewById(R.id.textView);
        manager=(WifiManager)self.getSystemService(Context.WIFI_SERVICE);
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                List<ScanResult> wifi=null;
                ;
                textView.setText("c");
                if(manager.startScan()){
                    wifi= manager.getScanResults();
                }
                if(wifi==null){
                    textView.setText("fail");
                }
                StringBuffer ssid=new StringBuffer();
                for(ScanResult result:wifi){
                    ssid.append(result.SSID+"\n");
                }
                textView.setText(ssid.toString());
                counter.counter++;
            }
        };
        timer.schedule(timerTask,5,1000);
        //Toast.makeText(this,ssid,Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        if(counter.counter<10) {
            Intent start = new Intent(this, MainActivity.class);
            this.startActivity(start);
        }
        super.onDestroy();
        getDelegate().onDestroy();
    }
}
