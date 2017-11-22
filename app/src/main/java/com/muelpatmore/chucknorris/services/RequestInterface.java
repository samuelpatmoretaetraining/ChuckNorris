package com.muelpatmore.chucknorris.services;

import com.muelpatmore.chucknorris.models.ChuckModel;
import com.muelpatmore.chucknorris.utils.API_List;

import io.reactivex.Observable;

import retrofit2.http.GET;

/**
 * Created by Samuel on 22/11/2017.
 */

public interface RequestInterface {

    @GET(API_List.Chuck_API)
    Observable<ChuckModel> getChuckList();
}
