package com.example.nive_pt1681.recipeapplication.activity;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nive_pt1681.recipeapplication.R;
import com.example.nive_pt1681.recipeapplication.adapter.AddRecipeImageAdapter;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ImageEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.IngredientsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.MealTimeCategoryEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.MethodsEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.RecipeEntry;
import com.example.nive_pt1681.recipeapplication.database.RecipeContract.ReferenceLinkEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by nive-pt1681 on 20/03/18.
 */

public class AddRecipeActivity extends AppCompatActivity {
    private LinearLayout parentOfReferenceLinks,parentOfMethods,parentOfIngredients;
    private ImageButton addMoreReferenceLinks,addMoreMethods,addMethodImage,addMoreIngredients,deleteMethodImage;
    private EditText referenceLink,methodsText,ingredients;
    private EditText servingsCount;
    private EditText cookingTime;
    private ImageView methodImage;
    private Button addImageButton;
    public static final int RESULT_PICK_IMAGE=0,RESULT_METHOD_IMAGE=1;
    private TabLayout mTabLayout;
    private PagerAdapter mAdapter;
    private ViewPager mViewPager;
    private Spinner shareTo,cookingLevel;
    private int shareSecurity=-1,mCookingLevel=-1;
    private RadioGroup diet;
    private ArrayList<Uri> imageUri;
    private EditText recipeTitle,recipeDescription;
    private int mealTimeCount=0;
    private int categoryCount=0;
    private Uri videoUri;
    private ArrayList<String> mealTimeChecked;
    private ArrayList<String> categoryChecked;
    private Button shootVideo,pickVideo;
    private static final int RESULT_FROM_PICK=2,RESULT_FROM_SHOOT=3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_layout);

        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Add Recipe");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mealTimeChecked=new ArrayList<>();
        categoryChecked=new ArrayList<>();

        addMoreReferenceLinks=findViewById(R.id.add_reference_links_button);
        parentOfReferenceLinks=findViewById(R.id.reference_links_container);
        parentOfIngredients=findViewById(R.id.ingredients_container);
        parentOfMethods=findViewById(R.id.methods_container);
        addMethodImage=findViewById(R.id.add_methods_image_button);
        addMoreMethods=findViewById(R.id.add_methods_button);
        addMoreIngredients=findViewById(R.id.add_ingredients_view);
        servingsCount=findViewById(R.id.servings_number);
        cookingTime=findViewById(R.id.cooking_time);
        addImageButton=findViewById(R.id.add_recipe_image_button);
        diet=findViewById(R.id.diet_radio_group);
        recipeTitle=findViewById(R.id.add_recipe_title);
        recipeDescription=findViewById(R.id.recipe_story);
        referenceLink=findViewById(R.id.reference_link);
        methodsText=findViewById(R.id.method_description);
        ingredients=findViewById(R.id.ingredient);
        shootVideo=findViewById(R.id.shoot_video_button);
        pickVideo=findViewById(R.id.add_video_button);

        shareTo=findViewById(R.id.recipe_security);
        cookingLevel=findViewById(R.id.add_recipe_cooking_level);

        mTabLayout=findViewById(R.id.tabDots);
        mViewPager=findViewById(R.id.pager);
        mTabLayout.setupWithViewPager(mViewPager);

        setUpSpinner();

        shootVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, RESULT_FROM_SHOOT);
                }
            }
        });

        pickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,RESULT_FROM_PICK);
            }
        });
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,RESULT_PICK_IMAGE);
            }
        });

        addMoreReferenceLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(referenceLink.getText().toString())){
                    LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                    View view=inflater.inflate(R.layout.single_reference_link_item,null);
                    parentOfReferenceLinks.addView(view,parentOfReferenceLinks.getChildCount());
                }
            }
        });

        addMoreMethods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(methodsText.getText().toString())){
                    LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                    View view=inflater.inflate(R.layout.add_single_method_item,null);
                    parentOfMethods.addView(view,parentOfMethods.getChildCount());
                }
            }
        });

        addMethodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(methodsText.getText().toString())){
                    LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                    View view=inflater.inflate(R.layout.add_method_image,null);
                    methodImage=view.findViewById(R.id.method_image);
                    Intent intent=new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,RESULT_METHOD_IMAGE);
                    parentOfMethods.addView(view,parentOfMethods.getChildCount());
                }
            }
        });


        addMoreIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(ingredients.getText().toString())){
                    LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                    View view=inflater.inflate(R.layout.add_ingredients,null);
                    parentOfIngredients.addView(view,parentOfIngredients.getChildCount());
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_recipe,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.post_recipe:
                postRecipe();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void postRecipe(){
        if(diet.getCheckedRadioButtonId()==-1){
            Toast.makeText(this,"Diet cannot be empty",Toast.LENGTH_SHORT).show();
            diet.requestFocus();
        }
        else if(shareSecurity==-1){
            Toast.makeText(this,"Share To cannot be empty",Toast.LENGTH_SHORT).show();
            shareTo.requestFocus();
        }
        else if(mCookingLevel==-1){
            Toast.makeText(this,"Cooking Level cannot be empty",Toast.LENGTH_SHORT).show();
            cookingLevel.requestFocus();
        }
        else if(TextUtils.isEmpty(recipeTitle.getText().toString())){
            Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_SHORT).show();
            recipeTitle.requestFocus();
        }
        else if(TextUtils.isEmpty(recipeDescription.getText().toString())){
            Toast.makeText(this,"Description cannot be empty",Toast.LENGTH_SHORT).show();
            recipeDescription.requestFocus();
        }
        else if(shareSecurity==-1){
            Toast.makeText(this,"Share To cannot be empty",Toast.LENGTH_SHORT).show();
            shareTo.requestFocus();
        }
        else if(mealTimeCount<=0){
            Toast.makeText(this,"MealTime cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else if(categoryCount<=0){
            Toast.makeText(this,"Category cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(servingsCount.getText().toString())){
            Toast.makeText(this,"Servings cannot be empty",Toast.LENGTH_SHORT).show();
            servingsCount.requestFocus();
        }
        else if(TextUtils.isEmpty(cookingTime.getText().toString())){
            Toast.makeText(this,"Cooking Time cannot be empty",Toast.LENGTH_SHORT).show();
            cookingTime.requestFocus();
        }
        else if(TextUtils.isEmpty(ingredients.getText().toString())){
            Toast.makeText(this,"Ingredients cannot be empty",Toast.LENGTH_SHORT).show();
            ingredients.requestFocus();
        }
        else if(TextUtils.isEmpty(methodsText.getText().toString())){
            Toast.makeText(this,"Methods cannot be empty",Toast.LENGTH_SHORT).show();
            methodsText.requestFocus();
        }
        else if(TextUtils.isEmpty(referenceLink.getText().toString())){
            Toast.makeText(this,"Reference links cannot be empty",Toast.LENGTH_SHORT).show();
            referenceLink.requestFocus();
        }
        else{
            ContentValues contentValues=new ContentValues();
            contentValues.put(RecipeEntry.RECIPE_EARNED_CREDITS,0);
            if(videoUri!=null){
                contentValues.put(RecipeEntry.RECIPE_VIDEO,videoUri.toString());
            }
            contentValues.put(RecipeEntry.RECIPE_NO_OF_LIKES,0);
            contentValues.put(RecipeEntry.RECIPE_DATE,new Date().getTime());
            contentValues.put(RecipeEntry.RECIPE_COOKING_LEVEL,mCookingLevel);

            int myDiet=RecipeEntry.RECIPE_DIET_VEGETARIAN;

            switch (diet.getCheckedRadioButtonId()){
                case R.id.diet_vegan:
                    myDiet=RecipeEntry.RECIPE_DIET_VEGAN;
                    break;
                case R.id.diet_veg:
                    myDiet=RecipeEntry.RECIPE_DIET_VEGETARIAN;
                    break;
                case R.id.diet_non_veg:
                    myDiet=RecipeEntry.RECIPE_DIET_NON_VEGETARIAN;
                    break;
            }
            contentValues.put(RecipeEntry.RECIPE_DIET,myDiet);
            contentValues.put(RecipeEntry.RECIPE_COOKING_TIME,cookingTime.getText().toString());
            contentValues.put(RecipeEntry.RECIPE_NO_OF_SERVINGS,Integer.parseInt(servingsCount.getText().toString()));
            contentValues.put(RecipeEntry.RECIPE_DESCRIPTION,recipeDescription.getText().toString());
            contentValues.put(RecipeEntry.RECIPE_SECURITY,shareSecurity);
            contentValues.put(RecipeEntry.RECIPE_TITLE,recipeTitle.getText().toString());
            contentValues.put(RecipeEntry.RECIPE_BY_USER_ID,HomeActivity.id);
            String recipeId=UUID.randomUUID().toString();
            contentValues.put(RecipeEntry.RECIPE_ID, recipeId);
            Cursor cursor=getContentResolver().query(RecipeEntry.CONTENT_URI,new String[]{"MAX("+RecipeEntry._ID+") AS MAX_ID"},null,null,null);
            if(cursor!=null && cursor.moveToFirst()){
                int count=cursor.getInt(0);
                contentValues.put(RecipeEntry._ID,count+1);
                Uri uri=getContentResolver().insert(RecipeEntry.CONTENT_URI,contentValues);
                if(uri!=null){
                    Log.d("me", "postRecipe: Inserted Successfully");
                }
            }

            contentValues=new ContentValues();
            cursor=getContentResolver().query(IngredientsEntry.CONTENT_URI,new String[]{"MAX("+IngredientsEntry._ID+") AS MAX_ID"},null,null,null);
            if(cursor!=null && cursor.moveToFirst()){
                int count=cursor.getInt(0);
                count++;
                int noOfChild=parentOfIngredients.getChildCount();
                for(int i=0;i<noOfChild;i++){
                    EditText ingredient=(EditText)parentOfIngredients.getChildAt(i);
                    contentValues.put(IngredientsEntry._ID,count+i);
                    contentValues.put(IngredientsEntry.INGREDIENTS_NAME,ingredient.getText().toString());
                    contentValues.put(IngredientsEntry.INGREDIENTS_RECIPE_ID,recipeId);
                    Uri uri=getContentResolver().insert(IngredientsEntry.CONTENT_URI,contentValues);
                    if(uri!=null){
                        Log.d("me", "postRecipe: Inserted Successfully");
                    }
                }
            }

            cursor=getContentResolver().query(MethodsEntry.CONTENT_URI,new String[]{"MAX("+MethodsEntry._ID+") AS MAX_ID"},null,null,null);
            if(cursor!=null && cursor.moveToFirst()){
                int count=cursor.getInt(0);
                count++;
                int noOfChild=parentOfMethods.getChildCount();
                contentValues=new ContentValues();
                for(int i=0;i<noOfChild;i++){
                    View view=parentOfMethods.getChildAt(i);
                    contentValues.put(MethodsEntry._ID,count+i);
                    contentValues.put(MethodsEntry.METHODS_POSITIONS,i+1);
                    contentValues.put(MethodsEntry.METHODS_RECIPE_ID,recipeId);
                    if(view.getClass().getName().equals("android.widget.LinearLayout")){
                        EditText methodDescription=view.findViewById(R.id.method_description);
                        contentValues.put(MethodsEntry.METHODS_DESCRIPTION,methodDescription.getText().toString());
                    }
                    else if(view.getClass().getName().equals("android.support.constraint.ConstraintLayout")){
                        ImageView imageView=view.findViewById(R.id.method_image);
                        contentValues.put(MethodsEntry.METHODS_DESCRIPTION,"#image#");
                        contentValues.put(MethodsEntry.METHODS_IMAGE,imageView.getTag().toString());
                    }
                    Uri uri=getContentResolver().insert(MethodsEntry.CONTENT_URI,contentValues);
                    if(uri!=null){
                        Log.d("me", "postRecipe: Inserted Successfully");
                    }
                }
            }

            if(imageUri.size()>0){
                contentValues=new ContentValues();
                cursor=getContentResolver().query(ImageEntry.CONTENT_URI,new String[]{"MAX("+ImageEntry._ID+") AS MAX_ID"},null,null,null);
                if(cursor!=null && cursor.moveToFirst()){
                    int count=cursor.getInt(0);
                    count++;
                    int noOfImages=imageUri.size();
                    for(int i=0;i<noOfImages;i++){
                        contentValues.put(ImageEntry._ID,count+i);
                        contentValues.put(ImageEntry.IMAGES_CATEGORY,ImageEntry.CATEGORY_RECIPE);
                        contentValues.put(ImageEntry.IMAGES_ID,UUID.randomUUID().toString());
                        contentValues.put(ImageEntry.IMAGES_RECIPE_ID,recipeId);
                        contentValues.put(ImageEntry.IMAGES_URI,imageUri.get(i).toString());
                        Uri uri=getContentResolver().insert(ImageEntry.CONTENT_URI,contentValues);
                        if(uri!=null){
                            Log.d("me", "postRecipe: Inserted Successfully");
                        }
                    }
                }
            }

            contentValues=new ContentValues();
            cursor=getContentResolver().query(ReferenceLinkEntry.CONTENT_URI,new String[]{"MAX("+ ReferenceLinkEntry._ID+") AS MAX_ID"},null,null,null);
            if(cursor!=null && cursor.moveToFirst()){
                int count=cursor.getInt(0);
                count++;
                int noOfChild=parentOfReferenceLinks.getChildCount();
                for(int i=0;i<noOfChild;i++){
                    EditText referenceLink=(EditText)parentOfReferenceLinks.getChildAt(i);
                    contentValues.put(ReferenceLinkEntry._ID,count+i);
                    contentValues.put(ReferenceLinkEntry.REFERENCE_LINK,referenceLink.getText().toString());
                    contentValues.put(ReferenceLinkEntry.LINK_RECIPE_ID,recipeId);
                    Uri uri=getContentResolver().insert(ReferenceLinkEntry.CONTENT_URI,contentValues);
                    if(uri!=null){
                        Log.d("me", "postRecipe: Inserted Successfully");
                    }
                }
            }

            contentValues=new ContentValues();
            cursor=getContentResolver().query(MealTimeCategoryEntry.CONTENT_URI,new String[]{"MAX("+ ReferenceLinkEntry._ID+") AS MAX_ID"},null,null,null);
            if(cursor!=null && cursor.moveToFirst()){
                int count=cursor.getInt(0);
                count++;
                contentValues.put(MealTimeCategoryEntry._ID,count);
                contentValues.put(MealTimeCategoryEntry.CATEGORY_RECIPE_ID,recipeId);
                for(int i=0;i<mealTimeChecked.size();i++){
                    contentValues.put(mealTimeChecked.get(i),1);
                }
                for(int i=0;i<categoryChecked.size();i++){
                    contentValues.put(categoryChecked.get(i),1);
                }
                Uri uri=getContentResolver().insert(MealTimeCategoryEntry.CONTENT_URI,contentValues);
                if(uri!=null){
                    Log.d("me", "postRecipe: Inserted Successfully");
                }
            }
            finish();
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RESULT_PICK_IMAGE){
            if(resultCode==RESULT_OK){
                imageUri=new ArrayList<>();
                if(data.getData()!=null){
                    imageUri.add(data.getData());
                    Log.d("me", "onActivityResult: One image selected");
                }
                else{
                    if(data.getClipData()!=null){
                        ClipData clipData=data.getClipData();
                        for(int i=0;i<clipData.getItemCount();i++){
                            ClipData.Item item=clipData.getItemAt(i);
                            imageUri.add(item.getUri());
                        }
                        Log.d("me", "onActivityResult: Number of images selected are "+imageUri.size());
                    }
                }
                mAdapter=new AddRecipeImageAdapter(this,imageUri);
                mViewPager.setAdapter(mAdapter);
            }
        }
        if(requestCode==RESULT_METHOD_IMAGE){
            if(resultCode==RESULT_OK){
                if(data.getData()!=null){
                    Glide.with(this)
                            .load(data.getData())
                            .into(methodImage);
                    methodImage.setTag(data.getData().toString());
                }
            }
            else{
                methodImage.setVisibility(View.GONE);
                deleteMethodImage.setVisibility(View.GONE);
            }
        }

        if(requestCode==RESULT_FROM_SHOOT || requestCode==RESULT_FROM_PICK){
            if(resultCode==RESULT_OK){
                if(data.getData()!=null){
                    videoUri=data.getData();
                    shootVideo.setVisibility(View.GONE);
                    pickVideo.setVisibility(View.GONE);
                    Toast.makeText(this,"Video Added successfully",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void setUpSpinner(){
        ArrayAdapter shareAdapter=ArrayAdapter.createFromResource(this,R.array.share_security,android.R.layout.simple_spinner_item);
        shareAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        shareTo.setAdapter(shareAdapter);
        if(shareSecurity!=-1){
            shareTo.setSelection(shareSecurity);
        }

        ArrayAdapter cookingLevelAdapter=ArrayAdapter.createFromResource(this,R.array.cooking_level_array,android.R.layout.simple_spinner_dropdown_item);
        cookingLevelAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        cookingLevel.setAdapter(cookingLevelAdapter);
        if(mCookingLevel!=-1){
            cookingLevel.setSelection(mCookingLevel);
        }

        shareTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        shareSecurity= RecipeEntry.SECURITY_PRIVATE;
                        break;
                    case 1:
                        shareSecurity= RecipeEntry.SECURITY_PUBLIC;
                        break;
                    case 2:
                        shareSecurity= RecipeEntry.SECURITY_MY_RECIPES;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                shareSecurity= RecipeEntry.SECURITY_MY_RECIPES;
            }
        });

        cookingLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:
                        mCookingLevel= RecipeContract.UserEntry.LEVEL_BEGINNER;
                        break;
                    case 2:
                        mCookingLevel= RecipeContract.UserEntry.LEVEL_INTERMEDIATE;
                        break;
                    case 3:
                        mCookingLevel= RecipeContract.UserEntry.LEVEL_EXPERT;
                        break;
                    default:
                        mCookingLevel= RecipeContract.UserEntry.LEVEL_DEFAULT;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mCookingLevel= RecipeContract.UserEntry.LEVEL_BEGINNER;
            }
        });
    }

    public void deleteMethodImage(View view) {
        parentOfMethods.removeView((View)view.getParent());
    }

    public void changeInMealTime(View view) {
        boolean checked;
        CheckBox checkBox=(CheckBox)view;
        if(checkBox.isChecked()){
            mealTimeCount++;
            checked=true;
        }
        else{
            mealTimeCount--;
            checked=false;
        }
        switch (view.getId()){
            case R.id.meal_time_breakfast:
                if(checked)
                    mealTimeChecked.add(MealTimeCategoryEntry.MEAL_TIME_BREAKFAST);
                else
                    mealTimeChecked.remove(MealTimeCategoryEntry.MEAL_TIME_BREAKFAST);
                break;
            case R.id.meal_time_brunch:
                if(checked)
                    mealTimeChecked.add(MealTimeCategoryEntry.MEAL_TIME_BRUNCH);
                else
                    mealTimeChecked.remove(MealTimeCategoryEntry.MEAL_TIME_BRUNCH);
                break;
            case R.id.meal_time_lunch:
                if(checked)
                    mealTimeChecked.add(MealTimeCategoryEntry.MEAL_TIME_LUNCH);
                else
                    mealTimeChecked.remove(MealTimeCategoryEntry.MEAL_TIME_LUNCH);
                break;
            case R.id.meal_time_dinner:
                if(checked)
                    mealTimeChecked.add(MealTimeCategoryEntry.MEAL_TIME_DINNER);
                else
                    mealTimeChecked.remove(MealTimeCategoryEntry.MEAL_TIME_DINNER);
                break;
        }
    }

    public void changeInCategory(View view) {
        boolean checked;
        CheckBox checkBox=(CheckBox)view;
        if(checkBox.isChecked()){
            categoryCount++;
            checked=true;
        }
        else{
            categoryCount--;
            checked=false;
        }
        switch (view.getId()){
            case R.id.snacks:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_SNACKS);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_SNACKS);
                break;
            case R.id.appetiser:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_APPETISERS);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_APPETISERS);
                break;
            case R.id.soups:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_SOUPS);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_SOUPS);
                break;
            case R.id.noodles:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_NOODLES);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_NOODLES);
                break;
            case R.id.rice:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_RICE);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_RICE);
                break;
            case R.id.pasta:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_PASTA);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_PASTA);
                break;
            case R.id.meat:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_MEAT);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_MEAT);
                break;
            case R.id.poultry:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_POULTRY);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_POULTRY);
                break;
            case R.id.seafood:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_SEAFOOD);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_SEAFOOD);
                break;
            case R.id.salads:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_SALADS);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_SALADS);
                break;
            case R.id.sides:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_SIDES);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_SIDES);
                break;
            case R.id.sauces:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_SAUCES);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_SAUCES);
                break;
            case R.id.baking:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_BAKING);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_BAKING);
                break;
            case R.id.desserts:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_DESSERTS);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_DESSERTS);
                break;
            case R.id.drinks:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_DRINKS);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_DRINKS);
                break;
            case R.id.cakes:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_CAKES);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_CAKES);
                break;
            case R.id.burger:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_BURGER);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_BURGER);
                break;
            case R.id.pizza:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_PIZZA);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_PIZZA);
                break;
            case R.id.gravy:
                if(checked)
                    categoryChecked.add(MealTimeCategoryEntry.CATEGORY_GRAVY);
                else
                    categoryChecked.remove(MealTimeCategoryEntry.CATEGORY_GRAVY);
                break;
        }
    }
}
