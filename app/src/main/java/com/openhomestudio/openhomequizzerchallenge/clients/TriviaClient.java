package com.openhomestudio.openhomequizzerchallenge.clients;

import com.openhomestudio.openhomequizzerchallenge.models.Mcq;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TriviaClient {

    @GET("/api.php?")
    Call<Mcq> getMcqs(
            @Query("amount") String amount,
            @Query("category") String category,
            @Query("difficulty") String difficulty,
            @Query("type") String type
    );

}
