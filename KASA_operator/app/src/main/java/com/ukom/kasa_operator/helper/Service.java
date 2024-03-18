package com.ukom.kasa_operator.helper;


import com.ukom.kasa_operator.model.KaryaModel;
import com.ukom.kasa_operator.model.LoginModel;
import com.ukom.kasa_operator.model.RegisterModel;
import com.ukom.kasa_operator.model.UbahStatusModel;
import com.ukom.kasa_operator.response.KaryaResponse;
import com.ukom.kasa_operator.response.LoginResponse;
import com.ukom.kasa_operator.response.RegisterResponse;
import com.ukom.kasa_operator.response.UbahStatusResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
    //AUTH
    @POST("login")
    Call<LoginResponse> loginOperator(@Body LoginModel loginModel);

    @POST("register")
    Call<RegisterResponse> registerOperator(@Body RegisterModel registerModel);

    //KARYA
    @GET("karya/index")//get
    Call<List<KaryaModel>> getDataKarya();
    @GET("karya/index")//get
    Call<List<KaryaModel>> getDataKarya(@Query("keyword") String keyword);

    @GET("karya/ditolak")//get
    Call<List<KaryaModel>> karyaDitolak();
    @GET("karya/ditolak")//get
    Call<List<KaryaModel>> karyaDitolak(@Query("keyword") String keyword);

    @GET("karya/diterima")//get
    Call<List<KaryaModel>> karyaDiterima();
    @GET("karya/diterima")//get
    Call<List<KaryaModel>> karyaDiterima(@Query("keyword") String keyword);

    @POST("karya/change-status/diterima/{idkarya}")
    Call<Void> ubahStatusKaryaDiterima(@Path("idkarya") int idkarya);

    @POST("karya/change-status/ditolak/{idkarya}")
    Call<Void> ubahStatusKaryaDitolak(@Path("idkarya") int idkarya);




}
