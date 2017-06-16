package com.android.carton9.remotepc;

import java.io.*;
import java.net.*;
import java.util.Timer;

/**
 * Created by qazwq on 2017/2/22.
 */

public class WebData{
    private Socket client;
    public OutputStream out;
    private InputStream buf;
    private boolean isAlive;
    public WebData(){
        isAlive=false;
        client=null;
        out=null;
        buf=null;

    }
    public void init(){
        try {
            client = new Socket("192.168.1.109", 12345);
            client.setSoTimeout(10000);
            out = client.getOutputStream();
            buf = client.getInputStream();
            isAlive=true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void stop(){
        if(isAlive){
            try {
                out.close();
                client.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public WebData(Socket client){
        try {
            this.client = client;
            out = new PrintStream(client.getOutputStream());
            buf = client.getInputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void send(byte[] input) {
        if(isAlive){
            try {
                out.write(input);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public boolean tcpReceive(byte[] output){
        if(isAlive){
            try {
                buf.read(output);
                return true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}

