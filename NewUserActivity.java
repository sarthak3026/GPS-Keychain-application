package com.example.gpskeychain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class NewUserActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton back, menu;
    Button login;
    TextView create, name, cond;
    EditText fname, username, lname, pass;
    
    SQL db = new SQL(this);
    UserClass user = new UserClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        back = findViewById(R.id.b_a);
        back.setOnClickListener(this);
        menu = findViewById(R.id.m);
        menu.setOnClickListener(this);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        create = findViewById(R.id.create);
        name = findViewById(R.id.name);
        cond = findViewById(R.id.condition);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        username = findViewById(R.id.uname);
        pass = findViewById(R.id.password);
    }

    @Override
    public void onClick(View view) {
        if (view == login) {
            newUserLogin();
        }
        else if (view == back) {
            Intent i = new Intent(NewUserActivity.this, LoginActivity.class);
            startActivity(i);
        }
        else {
            Toast.makeText(this, "User already Registered", Toast.LENGTH_LONG).show();
        }
    }

    private void newUserLogin() {
        if (!db.checkUsername(username.getText().toString())) {
                user.setFname(fname.getText().toString().trim());
                user.setLname(lname.getText().toString().trim());
                user.setUsername(username.getText().toString().trim());
                user.setPassword(pass.getText().toString().trim());

                db.addUser(user);

            Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show();

               // Intent i = new Intent(NewUserActivity.this, MapActivity.class);
               // startActivity(i);
                SharedPreferences sp = getSharedPreferences("key", MODE_PRIVATE);
                SharedPreferences.Editor spe = sp.edit();
                spe.putString("fname", fname.getText().toString());
                spe.apply();
        }
        else
            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
    }
}