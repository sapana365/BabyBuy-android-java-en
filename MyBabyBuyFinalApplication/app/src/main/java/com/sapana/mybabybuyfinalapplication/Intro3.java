package com.sapana.mybabybuyfinalapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Intro3 extends AppCompatActivity {
    Button skipButton,nextbtn,backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro3);
        getSupportActionBar().hide();

        nextbtn = findViewById(R.id.nextbtn);
        skipButton = findViewById(R.id.skipButton);
        backbtn = findViewById(R.id.backbtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intro3.this, Intro4.class);
                startActivity(intent);

            }
        });
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intro3.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intro3.this, Intro2.class);
                startActivity(intent);
            }
        });
    }
}