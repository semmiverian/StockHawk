package com.sam_chordas.android.stockhawk.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Semmiverian on 6/18/16.
 */
public interface RetrofitInterface {

    @GET("yql")
    Call<Stock> checkStockSymbol(@Query(value = "q",encoded = true) String query, @Query("format") String format, @Query("true") String diagnostics, @Query("env") String env);

    @GET("yql")
    Call<GraphStock> checkGraphSymbol(@Query(value = "q",encoded = true) String query, @Query("format") String format, @Query("true") String diagnostics, @Query("env") String env);

}
