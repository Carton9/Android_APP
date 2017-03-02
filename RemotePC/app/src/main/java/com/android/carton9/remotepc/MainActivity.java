package com.android.carton9.remotepc;

import android.content.Intent;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.support.v4.view.ViewCompat.setTranslationX;
import static android.support.v4.view.ViewCompat.setTranslationY;

public class MainActivity extends AppCompatActivity {
    private GestureDetector mGesture;
    MainActivity self=this;
    TextView text;
    boolean isConnect;
    ConnectService connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=$(R.id.textView);
        Button a=$(R.id.button);
        Button b=$(R.id.button2);
        /*connect=new ConnectService(this);
        connect.connect();
        if(!connect.isConnect()){
            text.setText("error");
            return;
        }*/
        connect=new ConnectService(self);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.disconnect();
                connect.stop();
            }
        });
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.connect();
            }
        });
        mGesture = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e){
                StringBuffer string=new StringBuffer();
               // string.append(e.getRawX());
                //string.append(e.getRawY());
                //string.append(e.getSize());

                String isLeft=(e.getRawY()<1500)?"true":"false";
                String data=new String(0+"/"+0+"/true/false/"+isLeft+"$");
                //string.append(data);
                //text.setText(string);
                try {
                    if(connect.isConnect())
                        connect.getService().getData(data);
                } catch (RemoteException f) {
                    f.printStackTrace();
                }
                return true;
            }
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //根据手势拖拽控件的相位而移动控件
                String data=new String(distanceX+"/"+distanceY+"/false/false/false$");
                try {
                    if(connect.isConnect())
                    connect.getService().getData(data);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    private <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mGesture.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
