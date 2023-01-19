package com.sapana.mybabybuyfinalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class Feedback extends AppCompatActivity {

    Button btn_send;
    BottomNavigationView nav;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().hide();

        btn_send = (Button) findViewById(R.id.btn_send);
        fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Feedback.this, AddActivity.class);
                startActivity(intent);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback of application");
                intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
                intent.setData(Uri.parse("mailto:BabyBuy@gmail.com")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }

        });
        nav = findViewById(R.id.bottomNavigationView);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        Intent intentt = new Intent(Feedback.this,HomeActivity.class);
                        startActivity(intentt);
                        break;
                    case R.id.List:
                        Intent intent = new Intent(Feedback.this,DisplayData.class);
                        startActivity(intent);
                        break;
                    case R.id.feedback:
                        Intent intent3 = new Intent(Feedback.this,Feedback.class);
                        startActivity(intent3);
                        break;
                    case R.id.logout:
                        Intent intent4 = new Intent(Feedback.this,LoginActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.placeholder:
                        Intent intent5 = new Intent(Feedback.this,AddActivity.class);
                        startActivity(intent5);
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });

    }
}