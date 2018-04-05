package com.example.nive_pt1681.recipeapplication.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.UserEntry;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nive-pt1681 on 21/02/18.
 */

public class SignUpActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mMailId;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Spinner mRegionSpinner;
    private Spinner mCookingLevelSpinner;
    private Button mSignUp;
    public static final String TAG="me";

    private int mRegion=UserEntry.REGION_DEFAULT;
    private int mCookingLevel=UserEntry.LEVEL_DEFAULT;
    private Uri mUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Create an Account");
        setSupportActionBar(toolbar);

        mName=findViewById(R.id.name);
        mMailId=findViewById(R.id.mail);
        mPassword=findViewById(R.id.password);
        mConfirmPassword=findViewById(R.id.check_password);
        mRegionSpinner=findViewById(R.id.region);
        mCookingLevelSpinner=findViewById(R.id.cooking_level);
        mSignUp=findViewById(R.id.sign_up);

        setUpSpinner();

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int done=0;
                String name=mName.getText().toString();
                String mailId=mMailId.getText().toString();
                String password=mPassword.getText().toString();
                String confirmPassword=mConfirmPassword.getText().toString();

                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(mailId);

                String[] columns={UserEntry._ID,UserEntry.USER_EMAIL,UserEntry.USER_PASSWORD};
                Cursor cursor=getContentResolver().query(UserEntry.CONTENT_URI,columns,UserEntry.USER_EMAIL+"=?",new String[]{mailId},null);

                if(TextUtils.isEmpty(name)){
                    mName.setError(getString(R.string.name_error));
                }
                else if(TextUtils.isEmpty(password)){
                    mPassword.setError(getString(R.string.password_error));
                }
                else if(TextUtils.isEmpty(confirmPassword)){
                    mConfirmPassword.setError(getString(R.string.password_error));
                }
                else if(TextUtils.isEmpty(mailId)){
                    mMailId.setError(getString(R.string.mail_empty_error));
                }
                else if(!TextUtils.isEmpty(mailId) && cursor!=null && cursor.moveToFirst()){
                    mMailId.setError(getString(R.string.already_registered));
                }
                else if(mRegion==0){
                    TextView textView=findViewById(R.id.region_textView);
                    textView.setError(getString(R.string.region_error));
                }
                else if(mCookingLevel==0){
                    TextView textView=findViewById(R.id.cooking_level_textView);
                    textView.setError(getString(R.string.cooling_level_error));
                }
                else if(!TextUtils.equals(password,confirmPassword)){
                    mConfirmPassword.setError(getString(R.string.password_mismatch));
                    mPassword.setError(getString(R.string.password_mismatch));
                }
                else if(!(matcher.matches())){
                    mMailId.setError(getString(R.string.no_mail));
                }
                else{
                    done=1;
                }

                if(done==1){
                    int count=0;
                    ContentValues contentValues=new ContentValues();
                    Cursor cursor1=getContentResolver().query(UserEntry.CONTENT_URI,new String[]{"MAX("+ UserEntry._ID+") AS max_id"},null,null,null);
                    if(cursor1!=null && cursor1.moveToFirst()){
                        count=cursor1.getInt(0);
                    }
                    contentValues.put(UserEntry._ID,count+1);
                    contentValues.put(UserEntry.USER_ID,UUID.randomUUID().toString());
                    contentValues.put(UserEntry.USER_NAME,name);
                    contentValues.put(UserEntry.USER_EMAIL,mailId);
                    contentValues.put(UserEntry.USER_PASSWORD,password);
                    contentValues.put(UserEntry.USER_REGION,mRegion);
                    contentValues.put(UserEntry.USER_COOKING_LEVEL,mCookingLevel);
                    contentValues.put(UserEntry.USER_CREDITS,0);
                    Toast.makeText(getApplicationContext(),"Inserted Successfully...",Toast.LENGTH_SHORT).show();

                    mUri=getContentResolver().insert(UserEntry.CONTENT_URI,contentValues);
                    Log.d(TAG, "onClick: " + ContentUris.parseId(mUri));
                    finish();
                }
            }
        });
    }

    public void setUpSpinner(){
        ArrayAdapter regionAdapter=ArrayAdapter.createFromResource(this,R.array.region_array,android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        mRegionSpinner.setAdapter(regionAdapter);
        if(mRegion!=0){
            mRegionSpinner.setSelection(mRegion);
        }

        ArrayAdapter cookingLevelAdapter=ArrayAdapter.createFromResource(this,R.array.cooking_level_array,android.R.layout.simple_spinner_dropdown_item);
        cookingLevelAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        mCookingLevelSpinner.setAdapter(cookingLevelAdapter);
        if(mCookingLevel!=0){
            mCookingLevelSpinner.setSelection(mCookingLevel);
        }

        mRegionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:
                        mRegion=UserEntry.REGION_CHINESE;
                        break;
                    case 2:
                        mRegion=UserEntry.REGION_ITALIAN;
                        break;
                    case 3:
                        mRegion=UserEntry.REGION_MEXICAN;
                        break;
                    case 4:
                        mRegion=UserEntry.REGION_NORTH_INDIAN;
                        break;
                    case 5:
                        mRegion=UserEntry.REGION_SOUTH_INDIAN;
                        break;
                    default:
                            mRegion=UserEntry.REGION_DEFAULT;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mRegion=UserEntry.REGION_DEFAULT;
            }
        });

        mCookingLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:
                        mCookingLevel=UserEntry.LEVEL_BEGINNER;
                        break;
                    case 2:
                        mCookingLevel=UserEntry.LEVEL_INTERMEDIATE;
                        break;
                    case 3:
                        mCookingLevel=UserEntry.LEVEL_EXPERT;
                        break;
                    default:
                            mCookingLevel=UserEntry.LEVEL_DEFAULT;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mCookingLevel=UserEntry.LEVEL_DEFAULT;
            }
        });
    }



}