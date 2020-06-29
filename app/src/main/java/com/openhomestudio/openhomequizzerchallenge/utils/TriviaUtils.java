package com.openhomestudio.openhomequizzerchallenge.utils;

import com.openhomestudio.openhomequizzerchallenge.clients.TriviaClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TriviaUtils {

    private static String API_BASE_URL = "https://opentdb.com/";

    public static TriviaClient getTriviaClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder.client(
                        httpClient.build()
                )
                        .build();

        TriviaClient client = retrofit.create(TriviaClient.class);

        return client;
    }

}
