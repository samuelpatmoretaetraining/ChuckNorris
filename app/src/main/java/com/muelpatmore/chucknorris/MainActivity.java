package com.muelpatmore.chucknorris;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.muelpatmore.chucknorris.models.ChuckModel;
import com.muelpatmore.chucknorris.models.JokeModel;
import com.muelpatmore.chucknorris.services.RequestInterface;
import com.muelpatmore.chucknorris.services.ServerConnection;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActiviy";

    private RequestInterface requestInterface;
    private ArrayList<JokeModel> jokes;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateCache();
    }

    private void updateCache() {
        Log.i("updateCache", "start");
        Log.i("updateCache", "getServerConnection");
        requestInterface = ServerConnection.getServerConnection();
        Log.i("updateCache", "getChuckList");
        requestInterface.getChuckList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ChuckModel>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(ChuckModel value) {
                        jokes = new ArrayList<>(value.getJokes());
                        Log.i("onNext", "Stored List length: "+jokes.size());
                        initRecyclerView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() { }
        });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager((getApplicationContext())));
        recyclerView.setAdapter((new JokeAdapter(jokes, R.layout.joke_card, getApplicationContext())));
    }


}
