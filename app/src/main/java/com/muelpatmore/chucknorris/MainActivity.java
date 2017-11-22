package com.muelpatmore.chucknorris;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.muelpatmore.chucknorris.models.ChuckModel;
import com.muelpatmore.chucknorris.models.JokeModel;
import com.muelpatmore.chucknorris.services.RequestInterface;
import com.muelpatmore.chucknorris.services.ServerConnection;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActiviy";

    private RequestInterface requestInterface;
    private ArrayList<JokeModel> jokes;
    private RecyclerView recyclerView;
    private JokeAdapter jokeAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Date date = new Date();
    private long cacheTimestamp = 0;
    private boolean connectedToNetwork = false;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        initListeners();
        updateCache();
        cacheTimestamp = date.getTime();
        Log.i(TAG, "Current time: "+cacheTimestamp);
    }

    private void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateJokes();
            }
        });
    }

    /**
     *
     */
    private void updateJokeList() {
        Log.i(TAG, "View Refreshed");
        jokeAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void updateCache() {isNetworkAvailable();}

    private void isNetworkAvailable() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override public void accept (Boolean isConnectedToInternet){
                        if(isConnectedToInternet) {
                            updateFromServer();
                        } else {
                            Toast.makeText(MainActivity.this, "Network failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateFromServer() {
        Log.i(TAG, "Network connection active");

        requestInterface = ServerConnection.getServerConnection();
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
        jokeAdapter = new JokeAdapter(jokes, R.layout.joke_card, getApplicationContext());
        recyclerView.setAdapter(jokeAdapter);
    }

    private void updateJokes() {
        long timeElapsed = (System.currentTimeMillis() - cacheTimestamp);
        Toast.makeText(this, timeElapsed + "ms since last update", Toast.LENGTH_SHORT).show();
        if(timeElapsed > 300000) {
            updateCache();
            cacheTimestamp = System.currentTimeMillis();
        }
        updateJokeList();
    }

}
