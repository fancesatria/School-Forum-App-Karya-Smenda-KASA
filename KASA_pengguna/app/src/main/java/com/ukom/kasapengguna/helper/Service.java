package com.ukom.kasapengguna.helper;

import com.ukom.kasapengguna.model.EventModel;
import com.ukom.kasapengguna.model.KaryaModel;
import com.ukom.kasapengguna.model.KategoriModel;
import com.ukom.kasapengguna.model.LoginModel;
import com.ukom.kasapengguna.model.RegisterModel;
import com.ukom.kasapengguna.model.UserModel;
import com.ukom.kasapengguna.response.ForgotPasswordResponse;
import com.ukom.kasapengguna.response.LoginResponse;
import com.ukom.kasapengguna.response.RegisterResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
    //AUTH
    @POST("login")
    Call<LoginResponse> login(@Body LoginModel loginModel);


    @POST("register")
    Call<RegisterResponse> register(@Body RegisterModel registerModel);

    @POST("forgot-password")
    Call<ForgotPasswordResponse> forgotPassword(@Body RegisterModel registerModel);


    //KATEGORI
    @GET("kategori/index")//get
    Call<List<KategoriModel>> getDataKategori();


    //EVENT
    @GET("event/index")//get
    Call<List<EventModel>> getDataEvent();

    @GET("event/index")//get
    Call<List<EventModel>> getDataEvent(@Query("keyword") String keyword);



    //KARYA
    @GET("karya/index")//get
    Call<List<KaryaModel>> getDataKarya();

    @GET("karya/index")//get
    Call<List<KaryaModel>> getDataKarya(@Query("keyword") String keyword);


    @GET("karya/show/{id}")//get
    Call<List<KaryaModel>> getDataKarya(@Path("id") int id);

    @Multipart
    @POST("karya/add")//add
    Call<KaryaModel> postKarya(@Part MultipartBody.Part gambar,
                               @Part("judul") RequestBody judul,
                               @Part("keterangan") RequestBody keterangan,
                               @Part("idkategori") RequestBody idkategori,
                               @Part("idpengguna") int idpengguna);

    @POST("karya/edit/{id}")//edit
    Call<KaryaModel> updateKarya(@Path("id") int id, @Body KaryaModel karyaModel);

    @GET("karya/del/{id}")//delete
    Call<KaryaModel> delKarya(@Path("id") int id);
}
