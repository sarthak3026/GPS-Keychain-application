package com.example.gpskeychain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    public int time=4000;
    ImageView i;
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        Toast.makeText(this,"FireBase connectivity success",Toast.LENGTH_LONG).show();
        i=findViewById(R.id.loc);
        t=findViewById(R.id.name);

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.bounce);
        i.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        },time);
    }
}
