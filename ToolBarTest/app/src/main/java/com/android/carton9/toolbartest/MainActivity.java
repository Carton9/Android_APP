package com.android.carton9.toolbartest;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    LinearLayout content;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        counter=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content=(LinearLayout) findViewById(R.id.linear);
        Button click=(Button) findViewById(R.id.button);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toolbar toolbar=new Toolbar(MainActivity.this);
                toolbar.setTitle(counter+"");
                toolbar.setBackgroundColor(0xFF00FF00);
                content.addView(toolbar,counter);
                counter++;
            }
        });
    }
}
