package edu.uta.leanmed.services;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vaibhav's Console on 3/9/2019.
 */

public class RetrofitService {
    private Retrofit retrofit;
    private static RetrofitService retrofitService;
    private static String url="http://www.playerpasslist.com/leanmed/api/";
    private static String urlLocalhost="http://localhost/leanmed/api/";
    private RetrofitService(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();
        retrofit=new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static Retrofit newInstance() {
        if(retrofitService==null)
            retrofitService=new RetrofitService();
        return retrofitService.retrofit;
    }
}