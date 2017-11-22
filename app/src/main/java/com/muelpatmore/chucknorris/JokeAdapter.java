package com.muelpatmore.chucknorris;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muelpatmore.chucknorris.models.JokeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 22/11/2017.
 */

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.MyViewHolder> {

    private ArrayList<JokeModel> jokeList;
    private int row_joke;
    private Context applicationContext;

    public JokeAdapter(ArrayList<JokeModel> jokeList, int row_joke, Context applicationContext) {
        this.jokeList = jokeList;
        this.row_joke = row_joke;
        this.applicationContext = applicationContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.joke_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        JokeModel joke = jokeList.get(position);
        holder.tvJoke.setText(joke.getJoke());
        holder.tvID.setText("ID: " + joke.getId());
        String catagories = " ";
        for (String c : joke.getCategories()) {
            catagories += " " + c;
        }
        holder.tvCatagory.setText(catagories);
    }

    @Override
    public int getItemCount() {
        return jokeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvJoke, tvCatagory, tvID;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvJoke= (TextView) itemView.findViewById(R.id.tvJoke);
            tvID= (TextView) itemView.findViewById(R.id.tvID);
            tvCatagory= (TextView) itemView.findViewById(R.id.tvCatagory);
        }
    }
}
