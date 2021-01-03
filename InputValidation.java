package com.example.gpskeychain;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class InputValidation {
    private Context context;

    public InputValidation(Context context){
        this.context=context;
    }

    public boolean isInputEditTextFilled(EditText email, String message){
        String value=email.getText().toString().trim();
        if(value.isEmpty()){
            Toast.makeText(context.getApplicationContext(),message,Toast.LENGTH_LONG).show();
            hideKeyboard(email);
            return false;
        }
        else
            return true;
    }
    public boolean isPassAndConfirmMatch(String e1,String e2,String message){
        if(!e1.equals(e2)){
            Toast.makeText(context.getApplicationContext(),message,Toast.LENGTH_LONG).show();
           // hideKeyboard(e2);
            return false;
        }
        else
            return true;
    }

    public void hideKeyboard(View v){
        InputMethodManager imm= (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(v.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
