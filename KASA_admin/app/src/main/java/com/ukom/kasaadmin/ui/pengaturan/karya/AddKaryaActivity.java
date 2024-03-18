package com.ukom.kasaadmin.ui.pengaturan.karya;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ukom.kasaadmin.R;
import com.ukom.kasaadmin.component.ErrorDialog;
import com.ukom.kasaadmin.component.SuccessDialog;
import com.ukom.kasaadmin.databinding.ActivityAddKaryaBinding;
import com.ukom.kasaadmin.helper.Api;
import com.ukom.kasaadmin.helper.RealPathUtil;
import com.ukom.kasaadmin.helper.SPHelper;
import com.ukom.kasaadmin.model.KaryaModel;
import com.ukom.kasaadmin.model.KategoriModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddKaryaActivity extends AppCompatActivity {
    ActivityAddKaryaBinding bind;
    String path;

    ArrayAdapter adapterKategori;
    List<KategoriModel> dataKategori = new ArrayList<>();//buat kategori
    List<String> kategori = new ArrayList<>();//buat selected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityAddKaryaBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        klikTombol();
        fetchKategori();
    }


    public void klikTombol(){
        bukaGaleri();
        klikTambah();
    }

    public void bukaGaleri(){
        bind.addGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                    Intent data = new Intent(Intent.ACTION_GET_CONTENT);
                    data.setType("image/*");
                    data = Intent.createChooser(data, "Pilih gambar");
                    sActivityRes.launch(data);

                } else {
                    ActivityCompat.requestPermissions(AddKaryaActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    Intent data = new Intent(Intent.ACTION_GET_CONTENT);
                    data.setType("image/*");
                    data = Intent.createChooser(data, "Pilih gambar");
                    sActivityRes.launch(data);
                }
                //Toast.makeText(AddKaryaActivity.this, "if scs", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void klikTambah(){
        bind.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPHelper sp = new SPHelper(AddKaryaActivity.this);
                String judul = bind.judulKarya.getText().toString();
                String keterangan = bind.keteranganKarya.getText().toString();
                if (judul.isEmpty() || keterangan.isEmpty()){
                    ErrorDialog.message(AddKaryaActivity.this, getString(R.string.empty), bind.getRoot());
                } else {
                    addKarya(judul, keterangan, kategoriSelected().getId(), sp.getIdPengguna());
                    //Toast.makeText(AddKaryaActivity.this, String.valueOf(kategoriSelected().getId()), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    ActivityResultLauncher<Intent> sActivityRes = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        Context context = AddKaryaActivity.this;
                        path = RealPathUtil.getRealPath(context, uri);
                        Toast.makeText(context, path, Toast.LENGTH_SHORT).show();
                        //Bitmap bitmap = BitmapFactory.decodeFile(path);
                        if(bind.imageView != null){
                            //bind.imageView.setImageBitmap(bitmap);
                            bind.imageView.setImageURI(uri);
                            bind.imageView.setVisibility(View.VISIBLE);
                        } else {
                            bind.imageView.setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(AddKaryaActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


    public void addKarya(String judul, String keterangan, int idkategori, int idpengguna){
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("gambar", file.getName(), requestFile);
        RequestBody upload_judul = RequestBody.create(MediaType.parse("multipart/form-data"), judul);
        RequestBody upload_keterangan = RequestBody.create(MediaType.parse("multipart/form-data"), keterangan);
        RequestBody upload_idkategori = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(idkategori));

        Call<KaryaModel> karyaModelCall = Api.getRetrofit(AddKaryaActivity.this).postKarya(body, upload_judul, upload_keterangan, upload_idkategori,idpengguna);
        karyaModelCall.enqueue(new Callback<KaryaModel>() {
            @Override
            public void onResponse(Call<KaryaModel> call, Response<KaryaModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus().toString().equals("200")){
                        SuccessDialog.message(AddKaryaActivity.this, getString(R.string.saved), bind.getRoot());
                    } else{
                        ErrorDialog.message(AddKaryaActivity.this, getString(R.string.unsaved), bind.getRoot());
                    }
                    KaryaFragment.getInstance().fetchData();
                }
            }

            @Override
            public void onFailure(Call<KaryaModel> call, Throwable t) {
                Toast.makeText(AddKaryaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void fetchKategori(){
        Call<List<KategoriModel>> call = Api.getRetrofit(AddKaryaActivity.this).getDataKategori();
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
                adapterKategori = new ArrayAdapter(AddKaryaActivity.this, android.R.layout.simple_spinner_dropdown_item, kategori);
                bind.opsiKategori.setAdapter(adapterKategori);

                //Toast.makeText(EditKaryaActivity.this, kategoriSelected().getKategori(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<KategoriModel>> call, Throwable t) {
                Toast.makeText(AddKaryaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    //buat ngambil
    public KategoriModel kategoriSelected(){
        return dataKategori.get(kategori.indexOf(bind.opsiKategori.getText().toString()));
    }
}