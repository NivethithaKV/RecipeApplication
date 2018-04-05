package com.example.nive_pt1681.recipeapplication.model;

import java.util.UUID;

/**
 * Created by nive-pt1681 on 22/02/18.
 */

public class Recipe {

    private UUID recipe_id;
    private int id;
    private String recipe_by_user_id;
    private String recipe_title;
    private int recipe_security;
    private String recipe_description;
    private int recipe_no_of_servings;
    private String recipe_cooking_time;
    private int recipe_diet;
    private int recipe_cooking_level;
    private long recipe_date;
    private int recipe_no_of_likes;
    private int recipe_earned_credits;
    private String recipe_image;

    public UUID getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(UUID recipe_id) {
        this.recipe_id = recipe_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipe_by_user_id() {
        return recipe_by_user_id;
    }

    public void setRecipe_by_user_id(String recipe_by_user_id) {
        this.recipe_by_user_id = recipe_by_user_id;
    }

    public String getRecipe_title() {
        return recipe_title;
    }

    public void setRecipe_title(String recipe_title) {
        this.recipe_title = recipe_title;
    }

    public int getRecipe_security() {
        return recipe_security;
    }

    public void setRecipe_security(int recipe_security) {
        this.recipe_security = recipe_security;
    }

    public String getRecipe_description() {
        return recipe_description;
    }

    public void setRecipe_description(String recipe_description) {
        this.recipe_description = recipe_description;
    }

    public int getRecipe_no_of_servings() {
        return recipe_no_of_servings;
    }

    public void setRecipe_no_of_servings(int recipe_no_of_servings) {
        this.recipe_no_of_servings = recipe_no_of_servings;
    }

    public String getRecipe_cooking_time() {
        return recipe_cooking_time;
    }

    public void setRecipe_cooking_time(String recipe_cooking_time) {
        this.recipe_cooking_time = recipe_cooking_time;
    }

    public int getRecipe_diet() {
        return recipe_diet;
    }

    public void setRecipe_diet(int recipe_diet) {
        this.recipe_diet = recipe_diet;
    }

    public int getRecipe_cooking_level() {
        return recipe_cooking_level;
    }

    public void setRecipe_cooking_level(int recipe_cooking_level) {
        this.recipe_cooking_level = recipe_cooking_level;
    }

    public long getRecipe_date() {
        return recipe_date;
    }

    public void setRecipe_date(long recipe_date) {
        this.recipe_date = recipe_date;
    }

    public int getRecipe_no_of_likes() {
        return recipe_no_of_likes;
    }

    public void setRecipe_no_of_likes(int recipe_no_of_likes) {
        this.recipe_no_of_likes = recipe_no_of_likes;
    }

    public int getRecipe_earned_credits() {
        return recipe_earned_credits;
    }

    public void setRecipe_earned_credits(int recipe_earned_credits) {
        this.recipe_earned_credits = recipe_earned_credits;
    }

    public String getRecipe_image() {
        return recipe_image;
    }

    public void setRecipe_image(String recipe_image) {
        this.recipe_image = recipe_image;
    }
}
