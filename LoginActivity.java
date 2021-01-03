package com.example.gpskeychain;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    TextView name,or;
    EditText username,pass;
    Button sign,login;
    SignInButton gmail;
    private int RC_NUM=1;

    SQL db=new SQL(LoginActivity.this);
    public InputValidation validate=new InputValidation(LoginActivity.this);

    String admin_uid="sarthak.sinha30";
    String admin_password="samarth7";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name=findViewById(R.id.name);
        or=findViewById(R.id.or);
        username=findViewById(R.id.username);
        pass=findViewById(R.id.password);
        sign=findViewById(R.id.sign);
        sign.setOnClickListener(this);
        login=findViewById(R.id.login);
        login.setOnClickListener(this);
        gmail=findViewById(R.id.google);
        gmail.setOnClickListener(this);
    }

    @Override
    @SuppressLint("MissingSuperCall")
    public void onStart() {
        super.onStart();
        GoogleSignInAccount gsa=GoogleSignIn.getLastSignedInAccount(this);
        updateUI(gsa);
    }

    public void updateUI(GoogleSignInAccount gsa){
        if(gsa !=null){
            Intent i=new Intent(LoginActivity.this,MapActivity.class);
            startActivity(i);
        }
    }

    public void signIN(){
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient client= GoogleSignIn.getClient(this,gso);
        Intent signin_flow=client.getSignInIntent();
        startActivityForResult(signin_flow,RC_NUM);
    }

    @Override
    public void onClick(View v) {
        if(v==sign){
            Intent i=new Intent(LoginActivity.this,NewUserActivity.class);
            startActivity(i);
        }
        else if(v==login){
            verifyFromSQLite();
        }
        else if (v==gmail){
            signIN();
        }
    }

    private void verifyFromSQLite() {
        if(validate.isInputEditTextFilled(username, getString(R.string.error_message_email))){
            if(db.checkUser(username.getText().toString().trim(),pass.getText().toString()) && adminVerify(username,pass)){
                Toast.makeText(this, "Logged in as Admin", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(LoginActivity.this,AdministraiveActivity.class);
                startActivity(i);
            }
            else if(db.checkUser(username.getText().toString().trim(), pass.getText().toString().trim())){

                SharedPreferences sp=getSharedPreferences("Key",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("username",username.getText().toString());
                editor.apply();

            //    db.useLatLong(username.getText().toString());

                Intent i=new Intent(LoginActivity.this,MapActivity.class);
                username.setText(null);
                pass.setText(null);
                startActivity(i);
            }
            else{
                String get_string_email_pass="Invalid Email or Password";
                Toast.makeText(this,get_string_email_pass,Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean adminVerify(EditText username,EditText pass) {
        String userid=username.getText().toString().trim();
        String password=pass.getText().toString().trim();
        return userid.equals(admin_uid) && password.equals(admin_password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_NUM){
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            assert result != null;
            if(result.isSuccess()){
                Intent i=new Intent(LoginActivity.this,MapActivity.class);
                startActivity(i);
                Toast.makeText(this,"Login successful",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this,"Login Unsuccessful",Toast.LENGTH_LONG).show();
            }
        }
    }
}