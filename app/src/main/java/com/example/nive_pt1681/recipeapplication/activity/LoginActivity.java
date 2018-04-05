package com.example.nive_pt1681.recipeapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.UserEntry;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    private EditText mMailId;
    private EditText mPassword;
    private Button mLogin;
    public static final String PREFS_NAME="MY_PREFS_FILE";
    public static final String PREFS_MAIL="mailId";
    public static final String PREFS_PASSWORD="password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String id=null;
        super.onCreate(savedInstanceState);
        SharedPreferences preferences=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String mail=preferences.getString(PREFS_MAIL,null);
        String password=preferences.getString(PREFS_PASSWORD,null);
        setContentView(R.layout.login_layout);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMailId=findViewById(R.id.login_mail);
        mPassword=findViewById(R.id.login_password);
        mLogin=findViewById(R.id.login);
        if(mail!=null && password!=null){
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            Cursor cursor=getContentResolver().query(UserEntry.CONTENT_URI,null,UserEntry.USER_EMAIL+"=?",new String[]{mail},null);
            if(cursor!=null && cursor.moveToFirst()){
                id =cursor.getString(cursor.getColumnIndex(UserEntry.USER_ID));
            }
            intent.putExtra("user_id", id);
            startActivity(intent);
        }

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor=getContentResolver().query(UserEntry.CONTENT_URI,null,UserEntry.USER_EMAIL+"=?",new String[]{mMailId.getText().toString()},null);
                if(cursor!=null && cursor.moveToFirst()){
                    String password=cursor.getString(cursor.getColumnIndex(UserEntry.USER_PASSWORD));
                    if(password.equals(mPassword.getText().toString())){
                        getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                                .edit()
                                .putString(PREFS_MAIL,mMailId.getText().toString())
                                .putString(PREFS_PASSWORD,mPassword.getText().toString())
                                .apply();
                        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                        String id =cursor.getString(cursor.getColumnIndex(UserEntry.USER_ID));
                        intent.putExtra("user_id", id);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        mPassword.setError(getString(R.string.wrong_password));
                    }
                }
                else{
                    mMailId.setError(getString(R.string.no_mail));
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMailId.setText("");
        mPassword.setText("");
    }

    public void signUpPage(View view) {
        Intent intent=new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
