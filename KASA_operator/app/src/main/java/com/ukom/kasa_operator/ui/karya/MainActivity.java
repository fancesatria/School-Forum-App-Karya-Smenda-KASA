package com.ukom.kasa_operator.ui.karya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ukom.kasa_operator.AboutUsActivity;
import com.ukom.kasa_operator.PrivacyPolicyActivity;
import com.ukom.kasa_operator.R;
import com.ukom.kasa_operator.TermAndConditions;
import com.ukom.kasa_operator.adapter.KaryaAdapter;
import com.ukom.kasa_operator.component.ErrorDialog;
import com.ukom.kasa_operator.component.LoadingDialog;
import com.ukom.kasa_operator.component.SuccessDialog;
import com.ukom.kasa_operator.databinding.ActivityMainBinding;
import com.ukom.kasa_operator.databinding.DialogNavHeaderBinding;
import com.ukom.kasa_operator.helper.Api;
import com.ukom.kasa_operator.helper.SPHelper;
import com.ukom.kasa_operator.model.KaryaModel;
import com.ukom.kasa_operator.model.UbahStatusModel;
import com.ukom.kasa_operator.response.KaryaResponse;
import com.ukom.kasa_operator.response.UbahStatusResponse;
import com.ukom.kasa_operator.ui.auth.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import hivatec.ir.easywebservice.EasyWebservice;
import hivatec.ir.easywebservice.Method;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    List<KaryaModel> data = new ArrayList<>();
    KaryaAdapter adapter;
    ActivityMainBinding bind;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        bind.item.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        fetchData();
        cari();
    }


    public void ubahStatusDiterima(int idkarya){
        LoadingDialog.load(MainActivity.this);
        Call<Void> karyaModelCall = Api.getRetrofit(MainActivity.this).ubahStatusKaryaDiterima(idkarya);
        karyaModelCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LoadingDialog.close();
                if (response.isSuccessful()){
                    SuccessDialog.message(MainActivity.this, getString(R.string.saved), bind.getRoot());
                    adapter = new KaryaAdapter(MainActivity.this, data);
                    adapter.notifyDataSetChanged();
                } else {
                    ErrorDialog.message(MainActivity.this,  getString(R.string.unsaved), bind.getRoot());
                }

                fetchData();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ubahStatusDitolak(int idkarya){
        LoadingDialog.load(MainActivity.this);
        Call<Void> ubahStatusResponseCall = Api.getRetrofit(MainActivity.this).ubahStatusKaryaDitolak(idkarya);
        ubahStatusResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LoadingDialog.close();
                if (response.isSuccessful()){
                    SuccessDialog.message(MainActivity.this, getString(R.string.saved), bind.getRoot());
                    adapter = new KaryaAdapter(MainActivity.this, data);
                    adapter.notifyDataSetChanged();
                } else {
                    ErrorDialog.message(MainActivity.this,  getString(R.string.unsaved), bind.getRoot());
//                    Log.d("ERROR RE", "onResponse: "+ response.raw().request().url().toString());
//                    Toast.makeText(MainActivity.this, response.raw().request().url().toString()+"", Toast.LENGTH_SHORT).show();
                }

                fetchData();


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(MainActivity.this,"error :"+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void menu(View view) {
        SPHelper sp = new SPHelper(MainActivity.this);
        DialogNavHeaderBinding binder = DialogNavHeaderBinding.inflate(LayoutInflater.from(MainActivity.this));
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        alertBuilder.setView(binder.getRoot());
        AlertDialog dialog = alertBuilder.create();

        binder.btnDiterima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DiterimaActivity.class));
            }
        });

        binder.btnDitolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DitolakActivity.class));
            }
        });

        binder.btnAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
            }
        });

        binder.btnSnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TermAndConditions.class));
            }
        });

        binder.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        binder.btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
            }
        });

        binder.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Konfirmasi").setMessage(R.string.logout_ensure)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sp.clearData();

                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            }
        });
        dialog.show();
    }

    public void fetchData(){
        String keyword = bind.searhview.getQuery().toString();
        LoadingDialog.load(MainActivity.this);
        Call<List<KaryaModel>> karyaGetRespCall = Api.getRetrofit(MainActivity.this).getDataKarya(keyword);
        karyaGetRespCall.enqueue(new Callback<List<KaryaModel>>() {
            @Override
            public void onResponse(Call<List<KaryaModel>> call, Response<List<KaryaModel>> response) {
                LoadingDialog.close();
                if(response.isSuccessful()){
                    data = response.body();
//                    bind.item.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    adapter = new KaryaAdapter(MainActivity.this, data);
                    bind.item.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


//                    Toast.makeText(MainActivity.this, String.valueOf(data.size()), Toast.LENGTH_SHORT).show();

                } else {
                    ErrorDialog.message(MainActivity.this, getString(R.string.cant_access), bind.getRoot());
                    //Toast.makeText(MainActivity.this, response.body().getJudul(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<KaryaModel>> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cari(){
        bind.searhview.setFocusable(false);
        bind.searhview.setClickable(true);

        bind.searhview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    fetchData();
                }
                return false;
            }
        });
    }
}