package com.example.nive_pt1681.recipeapplication.model;

import java.util.UUID;

/**
 * Created by nive-pt1681 on 22/02/18.
 */

public class User {
    private UUID mUUID;
    private int user_id;
    private String name;
    private String password;
    private String email;
    private int region;
    private int cooking_level;
    private String image;
    private int credits;
    private int following;
    private int follower;

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getCooking_level() {
        return cooking_level;
    }

    public void setCooking_level(int cooking_level) {
        this.cooking_level = cooking_level;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }
}
