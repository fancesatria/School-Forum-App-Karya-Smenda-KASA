package com.ukom.kasaadmin.helper;

import com.ukom.kasaadmin.model.EventModel;
import com.ukom.kasaadmin.model.KaryaModel;
import com.ukom.kasaadmin.model.KategoriModel;
import com.ukom.kasaadmin.model.LoginModel;
import com.ukom.kasaadmin.model.ModelBarang;
import com.ukom.kasaadmin.model.RegisterModel;
import com.ukom.kasaadmin.model.UserModel;
import com.ukom.kasaadmin.response.EventResponse;
import com.ukom.kasaadmin.response.ForgotPasswordResponse;
import com.ukom.kasaadmin.response.KaryaResponse;
import com.ukom.kasaadmin.response.LoginResponse;
import com.ukom.kasaadmin.response.RegisterResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
    //AUTH
    @POST("login")
    Call<LoginResponse> loginAdmin(@Body LoginModel loginModel);

    @POST("register")
    Call<RegisterResponse> registerAdmin(@Body RegisterModel registerModel);

    @POST("forgot-password")
    Call<ForgotPasswordResponse> forgotPassword(@Body RegisterModel registerModel);

    @POST("edit/{id}")
    Call<UserModel> editProfile(@Path("id") int id, @Body UserModel userModel);

    @GET("show/{id}")
    Call<List<UserModel>> showUser(@Path("id") int id);


    //KATEGORI
    @GET("kategori/index")//get
    Call<List<KategoriModel>> getDataKategori();

    @GET("kategori/index")//get
    Call<List<KategoriModel>> getDataKategori(@Query("keyword") String keyword);

    @POST("kategori/add")//add
    Call<KategoriModel> postKategori(@Body KategoriModel kategoriModel);

    @POST("kategori/edit/{id}")//edit
    Call<KategoriModel> updateKategori(@Path("id") int id, @Body KategoriModel kategoriModel);

    @GET("kategori/del/{id}")//delete
    Call<KategoriModel> delKategori(@Path("id") int id);



    //EVENT
    @GET("event/index")//get
    Call<List<EventModel>> getDataEvent();

    @GET("event/index")//get
    Call<List<EventModel>> getDataEvent(@Query("keyword") String keyword);

    @POST("event/add")//add
    Call<EventModel> postEvent(@Body EventModel eventModel);

    @POST("event/edit/{id}")//edit
    Call<EventModel> updateEvent(@Path("id") int id, @Body EventModel eventModel);

    @GET("event/del/{id}")//de lete
    Call<EventModel> delEvent(@Path("id") int id);



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
