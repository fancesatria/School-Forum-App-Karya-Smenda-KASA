package com.ukom.kasa_operator.helper;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    public static String BASE_URL = "http://192.168.90.126:8000/api/operator/";

    public static Service getRetrofit(Context context){
        SPHelper sp = new SPHelper(context);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                String token = sp.getToken(); //sp.getValue("token2");//ini ambil token dr response di postman
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization",token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retro = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        Service service = retro.create(Service.class);

        return service;
    }

    public static Service getRetrofit(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                //String token = sp.getToken(); //sp.getValue("token2");//ini ambil token dr response di postman
                Request newRequest = chain.request().newBuilder()
                        //.addHeader("Authorization",token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retro = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        Service service = retro.create(Service.class);

        return service;
    }

}
