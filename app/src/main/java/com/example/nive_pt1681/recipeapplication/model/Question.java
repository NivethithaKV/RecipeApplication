package com.example.nive_pt1681.recipeapplication.model;

import java.util.UUID;

/**
 * Created by nive-pt1681 on 25/03/18.
 */

public class Question {

    private String postedByUserName;
    private String postedByUserImage;
    private String postedQuestion;
    private String questionImage;
    private long postedDate;
    private UUID questionId;

    public String getPostedByUserName() {
        return postedByUserName;
    }

    public void setPostedByUserName(String postedByUserName) {
        this.postedByUserName = postedByUserName;
    }

    public String getPostedByUserImage() {
        return postedByUserImage;
    }

    public void setPostedByUserImage(String postedByUserImage) {
        this.postedByUserImage = postedByUserImage;
    }

    public String getPostedQuestion() {
        return postedQuestion;
    }

    public void setPostedQuestion(String postedQuestion) {
        this.postedQuestion = postedQuestion;
    }

    public long getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(long postedDate) {
        this.postedDate = postedDate;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }
}
