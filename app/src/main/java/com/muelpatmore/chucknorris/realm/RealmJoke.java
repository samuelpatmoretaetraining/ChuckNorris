package com.muelpatmore.chucknorris.realm;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Class to store Jokes (probably about Chuck Norris) in a Realm database.
 */
public class RealmJoke extends RealmObject {

    @PrimaryKey
    private int id;
    private String joke;
    private ArrayList<String> catagories;

    public RealmJoke() {}

    public RealmJoke(int id, String joke, ArrayList<String> categories) {
        this.id = id;
        this.joke = joke;
        this.catagories = categories;
    }

    public RealmJoke(String id, String joke, List<String> catagoriesList) {
        this.id = Integer.parseInt(id);
        this.joke = joke;
        this.catagories = new ArrayList<>(catagoriesList);
        cleanCatagories(this.catagories);
    }

    private void cleanCatagories(ArrayList<String> cats) {
        if (cats.size() == 0) {
            return;
        }

        for (int i = cats.size() ; i > 0 ; i++) {
            if (cats.get(i).trim().equals("")) {
                cats.remove(i);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public ArrayList<String> getCatagories() {
        return catagories;
    }

    public void setCatagories(ArrayList<String> catagories) {
        this.catagories = catagories;
    }
}
