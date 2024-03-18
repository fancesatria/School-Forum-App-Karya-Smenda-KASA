package com.ukom.kasaadmin.ui.pengaturan.karya;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ukom.kasaadmin.R;
import com.ukom.kasaadmin.component.ErrorDialog;
import com.ukom.kasaadmin.component.LoadingDialog;
import com.ukom.kasaadmin.component.SuccessDialog;
import com.ukom.kasaadmin.databinding.ActivityDetailKaryaBinding;
import com.ukom.kasaadmin.helper.Api;
import com.ukom.kasaadmin.helper.SPHelper;
import com.ukom.kasaadmin.model.EventModel;
import com.ukom.kasaadmin.model.KaryaModel;
import com.ukom.kasaadmin.model.KategoriModel;
import com.ukom.kasaadmin.ui.pengaturan.MasterKategoriActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailKaryaActivity extends AppCompatActivity {
    ActivityDetailKaryaBinding bind;
    private int idpengguna, idkarya,idkategori;
    private String judul, keterangan, gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityDetailKaryaBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        load();
    }

    public void load(){
        idkarya = getIntent().getIntExtra("idkarya",0);
        idpengguna = getIntent().getIntExtra("idpengguna",0);
        idkategori = getIntent().getIntExtra("idkategori",0);
        judul = getIntent().getStringExtra("judul");
        gambar = getIntent().getStringExtra("gambar");
        keterangan = getIntent().getStringExtra("keterangan");

        SPHelper sp = new SPHelper(this);
        bind.judulKarya.setText(judul);
        Glide.with(this).load(gambar).into(bind.imageView1);
        bind.deskripsi.setText(keterangan);
        bind.pencipta.setText(sp.getUsername());


        if (idpengguna == sp.getIdPengguna()){
            bind.linearLayout4.setVisibility(View.VISIBLE);
        }
    }


    public void hapus(View view) {
        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(DetailKaryaActivity.this);
        alert.setTitle("Konfirmasi");
        alert.setMessage(R.string.del_ensure);
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoadingDialog.load(DetailKaryaActivity.this);
                Call<KaryaModel> kategoriModelCall = Api.getRetrofit(DetailKaryaActivity.this).delKarya(idkarya);
                kategoriModelCall.enqueue(new Callback<KaryaModel>() {
                    @Override
                    public void onResponse(Call<KaryaModel> call, Response<KaryaModel> response) {
                        LoadingDialog.close();
                        if (response.isSuccessful()){
                            SuccessDialog.message(DetailKaryaActivity.this, getString(R.string.deleted_success), bind.getRoot());

                        } else {
                            ErrorDialog.message(DetailKaryaActivity.this, getString(R.string.deleted_error), bind.getRoot());
                        }
                        //KaryaFragment.getInstance().fetchData();
                    }

                    @Override
                    public void onFailure(Call<KaryaModel> call, Throwable t) {
                        LoadingDialog.close();
                        Toast.makeText(DetailKaryaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    public void edit(View view) {
        Intent i = new Intent(DetailKaryaActivity.this, EditKaryaActivity.class);
        i.putExtra("idkarya", idkarya);
        i.putExtra("gambar", gambar);
        i.putExtra("idkategori", idkategori);
        i.putExtra("judul", judul);
        i.putExtra("keterangan", keterangan);
        startActivity(i);
    }
}