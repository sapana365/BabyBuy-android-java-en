package com.sapana.mybabybuyfinalapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Intro2 extends AppCompatActivity {
    Button skipButton,nextbtn,backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_intro2);
        nextbtn = findViewById(R.id.nextbtn);
        skipButton = findViewById(R.id.skipButton);
        backbtn = findViewById(R.id.backbtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intro2.this, Intro3.class);
                startActivity(intent);

            }
        });
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intro2.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intro2.this, Intro1.class);
                startActivity(intent);


            }
        });
    }
}