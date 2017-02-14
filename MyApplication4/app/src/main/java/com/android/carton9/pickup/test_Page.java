package com.android.carton9.pickup;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class test_Page extends AppCompatActivity {
    ConnectService connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__page);
        connect=new ConnectService(this);
        Button start=$(R.id.button);
        Button indata=$(R.id.button2);
        Button act=$(R.id.button3);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.connect();
            }
        });
        indata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(connect.isConnect());
                    connect.getService().sendInfo(0,0," "," ");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    connect.getService().action();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.disconnect();
                connect.stop();
            }
        });
    }
    private <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }
}
