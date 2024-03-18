package com.ukom.kasapengguna.ui.pengaturan.karya;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ukom.kasapengguna.R;
import com.ukom.kasapengguna.component.ErrorDialog;
import com.ukom.kasapengguna.component.SuccessDialog;
import com.ukom.kasapengguna.databinding.ActivityEditKaryaBinding;
import com.ukom.kasapengguna.helper.Api;
import com.ukom.kasapengguna.model.KaryaModel;
import com.ukom.kasapengguna.model.KategoriModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditKaryaActivity extends AppCompatActivity {

    private int idpengguna, idkarya,idkategori;
    private String judul, keterangan;
    ActivityEditKaryaBinding bind;

    ArrayAdapter adapterKategori;
    List<KategoriModel> dataKategori = new ArrayList<>();//buat kategori dr database
    List<String> kategori = new ArrayList<>();//buat selected
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityEditKaryaBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        load();
    }
    
    public void load(){
        fetchKategori();
        idkarya = getIntent().getIntExtra("idkarya",0);
        idpengguna = getIntent().getIntExtra("idpengguna",0);
        idkategori = getIntent().getIntExtra("idkategori",0);
        judul = getIntent().getStringExtra("judul");
        keterangan = getIntent().getStringExtra("keterangan");
        
        bind.judulKarya.setText(judul);
        bind.keteranganKarya.setText(keterangan);
        bind.opsiKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(EditKaryaActivity.this, kategoriSelected().getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void fetchKategori(){
        Call<List<KategoriModel>> call = Api.getRetrofit(EditKaryaActivity.this).getDataKategori();
        call.enqueue(new Callback<List<KategoriModel>>() {
            @Override
            public void onResponse(Call<List<KategoriModel>> call, Response<List<KategoriModel>> response) {
                dataKategori.addAll(response.body());
                for(KategoriModel kategorimodel : response.body()){
                    kategori.add(kategorimodel.getKategori());
                }
                if(kategori.size()>0 ){
                    bind.opsiKategori.setText(kategori.get(0));
                }
                adapterKategori = new ArrayAdapter(EditKaryaActivity.this, android.R.layout.simple_spinner_dropdown_item, kategori);
                bind.opsiKategori.setAdapter(adapterKategori);

                //Toast.makeText(EditKaryaActivity.this, kategoriSelected().getKategori(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<KategoriModel>> call, Throwable t) {
                Toast.makeText(EditKaryaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    //buat ngambil
    public KategoriModel kategoriSelected(){
        return dataKategori.get(kategori.indexOf(bind.opsiKategori.getText().toString()));
    }

    public void simpanPerubahan(View view) {
        String e_judul = bind.judulKarya.getText().toString();
        String e_keterangan = bind.keteranganKarya.getText().toString();

        AlertDialog.Builder alert = new AlertDialog.Builder(EditKaryaActivity.this);
        alert.setTitle("Konfirmasi");
        alert.setMessage("Simpan perubahan ?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (e_judul.isEmpty() || e_keterangan.isEmpty()){
                    ErrorDialog.message(EditKaryaActivity.this, getString(R.string.empty), bind.getRoot());
                } else {
                    KaryaModel km = new KaryaModel();
                    km.setJudul(e_judul);
                    km.setKeterangan(e_keterangan);
                    km.setIdkategori(kategoriSelected().getId());
                    Call<KaryaModel> karyaModelCall = Api.getRetrofit(EditKaryaActivity.this).updateKarya(idkarya,km);
                    karyaModelCall.enqueue(new Callback<KaryaModel>() {
                        @Override
                        public void onResponse(Call<KaryaModel> call, Response<KaryaModel> response) {
                            if(response.isSuccessful()){
                                SuccessDialog.message(EditKaryaActivity.this, getString(R.string.saved), bind.getRoot());
                            } else {
                                ErrorDialog.message(EditKaryaActivity.this, getString(R.string.unsaved), bind.getRoot());
                                Toast.makeText(EditKaryaActivity.this, response.code()+"", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<KaryaModel> call, Throwable t) {
                            Toast.makeText(EditKaryaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
}