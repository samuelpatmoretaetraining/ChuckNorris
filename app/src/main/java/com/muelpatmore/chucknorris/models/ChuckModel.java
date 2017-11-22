package com.muelpatmore.chucknorris.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChuckModel {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("value")
    @Expose
    private List<JokeModel> value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<JokeModel> getJokes() {
        return value;
    }

    public void setJokes(List<JokeModel> value) {
        this.value = value;
    }

}