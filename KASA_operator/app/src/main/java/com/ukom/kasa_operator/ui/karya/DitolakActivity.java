package com.ukom.kasa_operator.ui.karya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ukom.kasa_operator.AboutUsActivity;
import com.ukom.kasa_operator.R;
import com.ukom.kasa_operator.TermAndConditions;
import com.ukom.kasa_operator.adapter.DiterimaAdapter;
import com.ukom.kasa_operator.adapter.DitolakAdapter;
import com.ukom.kasa_operator.component.ErrorDialog;
import com.ukom.kasa_operator.component.LoadingDialog;
import com.ukom.kasa_operator.databinding.ActivityDiterimaBinding;
import com.ukom.kasa_operator.databinding.ActivityDitolakBinding;
import com.ukom.kasa_operator.helper.Api;
import com.ukom.kasa_operator.helper.SPHelper;
import com.ukom.kasa_operator.model.KaryaModel;
import com.ukom.kasa_operator.ui.auth.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DitolakActivity extends AppCompatActivity {
    List<KaryaModel> data = new ArrayList<>();
    DitolakAdapter adapter;
    ActivityDitolakBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityDitolakBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        fetchData();
        cari();
    }

    public void fetchData(){
        String keyword = bind.searhview.getQuery().toString();
        LoadingDialog.load(DitolakActivity.this);
        Call<List<KaryaModel>> karyaGetRespCall = Api.getRetrofit(this).karyaDitolak(keyword);
        karyaGetRespCall.enqueue(new Callback<List<KaryaModel>>() {
            @Override
            public void onResponse(Call<List<KaryaModel>> call, Response<List<KaryaModel>> response) {
                LoadingDialog.close();
                if(response.isSuccessful()){
                    data = response.body();
                    bind.item.setLayoutManager(new LinearLayoutManager(DitolakActivity.this));
                    adapter = new DitolakAdapter(DitolakActivity.this, data);
                    bind.item.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {

//                    ErrorDialog.message(DitolakActivity.this, getString(R.string.cant_access), bind.getRoot());
                    Toast.makeText(DitolakActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(DitolakActivity.this, String.valueOf(data.size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<KaryaModel>> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(DitolakActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void menu(View view) {
        SPHelper sp = new SPHelper(DitolakActivity.this);
        com.ukom.kasa_operator.databinding.DialogNavHeaderBinding binder = com.ukom.kasa_operator.databinding.DialogNavHeaderBinding.inflate(LayoutInflater.from(DitolakActivity.this));
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DitolakActivity.this);
        alertBuilder.setView(binder.getRoot());
        AlertDialog dialog = alertBuilder.create();

        binder.btnDiterima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DitolakActivity.this, DiterimaActivity.class));
            }
        });

        binder.btnDitolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DitolakActivity.this, DitolakActivity.class));
            }
        });

        binder.btnAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DitolakActivity.this, AboutUsActivity.class));
            }
        });

        binder.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DitolakActivity.this, MainActivity.class));
            }
        });

        binder.btnSnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DitolakActivity.this, TermAndConditions.class));
            }
        });

        binder.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DitolakActivity.this);
                alert.setTitle("Konfirmasi").setMessage(R.string.logout_ensure)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sp.clearData();

                                startActivity(new Intent(DitolakActivity.this, LoginActivity.class));
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