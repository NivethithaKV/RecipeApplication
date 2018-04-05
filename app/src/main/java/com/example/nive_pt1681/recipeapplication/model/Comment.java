package com.example.nive_pt1681.recipeapplication.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by nive-pt1681 on 21/03/18.
 */

public class Comment {
    private UUID commentId;
    private String recipeId;
    private String userImage;
    private String userName;
    private Date commentedDate;
    private String commentedText;
    private String commentedImage;

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCommentedDate() {
        return commentedDate;
    }

    public void setCommentedDate(Date commentedDate) {
        this.commentedDate = commentedDate;
    }

    public String getCommentedText() {
        return commentedText;
    }

    public void setCommentedText(String commentedText) {
        this.commentedText = commentedText;
    }

    public String getCommentedImage() {
        return commentedImage;
    }

    public void setCommentedImage(String commentedImage) {
        this.commentedImage = commentedImage;
    }

    public UUID getCommentId() {
        return commentId;
    }

    public void setCommentId(UUID commentId) {
        this.commentId = commentId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
}
