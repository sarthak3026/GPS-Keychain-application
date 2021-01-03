package com.example.gpskeychain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AdministraiveActivity extends AppCompatActivity implements View.OnClickListener {

    SQL db=new SQL(this);
    InputValidation validate=new InputValidation(AdministraiveActivity.this);
    UserClass user=new UserClass();

    TextView admin,login,hello;
    ImageView person;
    EditText keyid,userid;
    Button add,logout,fetchAll;

    String message="Key Id field is empty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administraive);

        init();
        initlistener();
    }

    public void init() {
        person=findViewById(R.id.person);
        hello=findViewById(R.id.hello);
        admin=findViewById(R.id.admin);
        login=findViewById(R.id.login_text);
        keyid=findViewById(R.id.key_id);
        userid=findViewById(R.id.userid);
        add=findViewById(R.id.add);
        logout=findViewById(R.id.logout);
        fetchAll=findViewById(R.id.All_Users);
    }

    public void initlistener() {
        add.setOnClickListener(this);
        logout.setOnClickListener(this);
        fetchAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==add){
            if(validate.isInputEditTextFilled(keyid,message)){
                String v1=keyid.getText().toString();
                String v2=userid.getText().toString();

                if(v1.equals("") && v2.equals("")){
                    v1="0";
                    v2="0";
                }
                int kid=Integer.parseInt(v1);
                int uid=Integer.parseInt(v2);

                user.setKeyId(kid);
                user.setId(uid);

                db.addKey(user);
                Toast.makeText(this,"Key added to the user", Toast.LENGTH_LONG).show();
            }
        }
        else if(v==logout){
            Intent i=new Intent(AdministraiveActivity.this,LoginActivity.class);
            startActivity(i);
        }
        else if(v==fetchAll){
            Intent i=new Intent(AdministraiveActivity.this,UserListActivity.class);
            startActivity(i);
        }
    }
}