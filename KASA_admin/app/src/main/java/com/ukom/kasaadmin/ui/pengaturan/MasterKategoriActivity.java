package com.ukom.kasaadmin.ui.pengaturan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ukom.kasaadmin.R;
import com.ukom.kasaadmin.adapter.KategoriAdapter;
//import com.ukom.kasaadmin.component.LoadingDialog;
import com.ukom.kasaadmin.component.ErrorDialog;
import com.ukom.kasaadmin.component.LoadingDialog;
import com.ukom.kasaadmin.component.SuccessDialog;
import com.ukom.kasaadmin.databinding.ActivityMasterKategoriBinding;
import com.ukom.kasaadmin.databinding.DialogAddKategoriBinding;
import com.ukom.kasaadmin.databinding.DialogEditKategoriBinding;
import com.ukom.kasaadmin.helper.Api;
import com.ukom.kasaadmin.model.KategoriModel;
import com.ukom.kasaadmin.model.ModelBarang;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterKategoriActivity extends AppCompatActivity {

    ActivityMasterKategoriBinding bind;
    List<KategoriModel> data = new ArrayList<>();
    KategoriAdapter adapter;

    //List<ModelBarang> data2 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMasterKategoriBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        fetchData();
        cari();
        //swipe(); tanpa swipe aja

    }

    public void swipe(){
        bind.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
    }

    public void fetchData(){
        //final RecyclerView item = findViewById(R.id.item); //ini fungsinya buat nyambungin ke xml di recycleviewnya
        bind.item.setLayoutManager(new LinearLayoutManager(this));
        bind.item.setHasFixedSize(true);
        String keyword = bind.searhview.getQuery().toString();

        Call<List<KategoriModel>> call = Api.getRetrofit(MasterKategoriActivity.this).getDataKategori(keyword);
        call.enqueue(new Callback<List<KategoriModel>>() {
            @Override
            public void onResponse(Call<List<KategoriModel>> call, Response<List<KategoriModel>> response) {
                List<KategoriModel> data = response.body();
                bind.item.setAdapter(new KategoriAdapter(MasterKategoriActivity.this, data));
                //untuk buat swiperefresh(dipanggil)
                //swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<KategoriModel>> call, Throwable t) {
                Toast.makeText(MasterKategoriActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void tambahKategoriButton(View view) {
        dialogTambahKategori();
    }

    public void dialogTambahKategori(){
        DialogAddKategoriBinding binding = DialogAddKategoriBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(binding.getRoot());
        alert.setTitle("Tambahkan Kategori Baru");
        AlertDialog  dialog = alert.create();
        //binding.txtAddKategori.setText(kategoriModel.getKategori());
        binding.btnAddKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.load(MasterKategoriActivity.this);
                //ErrorDialog.message(MasterKategoriActivity.this, "Coba", binding.getRoot());
                if (!binding.txtAddKategori.getText().toString().isEmpty()){
                    KategoriModel kategoriModel = new KategoriModel(binding.txtAddKategori.getText().toString());
                    Call<KategoriModel> kategoriModelCall = Api.getRetrofit(MasterKategoriActivity.this).postKategori(kategoriModel);
                    kategoriModelCall.enqueue(new Callback<KategoriModel>() {
                        @Override
                        public void onResponse(Call<KategoriModel> call, Response<KategoriModel> response) {
                            LoadingDialog.close();
                            if (response.isSuccessful()){
                                SuccessDialog.message(MasterKategoriActivity.this, getString(R.string.saved), bind.getRoot());

                            } else {
                                ErrorDialog.message(MasterKategoriActivity.this, getString(R.string.unsaved), bind.getRoot());
                            }
                            fetchData();
                        }

                        @Override
                        public void onFailure(Call<KategoriModel> call, Throwable t) {
                            LoadingDialog.close();
                            Toast.makeText(MasterKategoriActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    ErrorDialog.message(MasterKategoriActivity.this, getString(R.string.empty), bind.getRoot());
                }
            }
        });
        dialog.show();
    }

    public void dialogEditKategori(int id, KategoriModel kategoriModel){
        DialogEditKategoriBinding binding = DialogEditKategoriBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(binding.getRoot());
        alert.setTitle("Ubah kategori");
        AlertDialog dialog = alert.create();
        binding.txtKategori.setText(kategoriModel.getKategori());
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.load(MasterKategoriActivity.this);
                if (!binding.txtKategori.getText().toString().isEmpty()){
                    KategoriModel kategoriModel = new KategoriModel(binding.txtKategori.getText().toString());
                    Call<KategoriModel> kategoriModelCall = Api.getRetrofit(MasterKategoriActivity.this).updateKategori(id, kategoriModel);
                    kategoriModelCall.enqueue(new Callback<KategoriModel>() {
                        @Override
                        public void onResponse(Call<KategoriModel> call, Response<KategoriModel> response) {
                            LoadingDialog.close();
                            if(response.isSuccessful()){
                                SuccessDialog.message(MasterKategoriActivity.this, getString(R.string.saved), bind.getRoot());
                            } else {
                                ErrorDialog.message(MasterKategoriActivity.this, getString(R.string.unsaved), bind.getRoot());
                            }
                            fetchData();
                        }

                        @Override
                        public void onFailure(Call<KategoriModel> call, Throwable t) {
                            LoadingDialog.close();
                            Toast.makeText(MasterKategoriActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    ErrorDialog.message(MasterKategoriActivity.this, getString(R.string.empty), bind.getRoot());
                }
            }
        });
        dialog.show();
    }

    public void dialogHapusKategori(int id){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Konfirmasi");
        alert.setMessage(R.string.del_ensure);
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoadingDialog.load(MasterKategoriActivity.this);
                Call<KategoriModel> kategoriModelCall = Api.getRetrofit(MasterKategoriActivity.this).delKategori(id);
                kategoriModelCall.enqueue(new Callback<KategoriModel>() {
                    @Override
                    public void onResponse(Call<KategoriModel> call, Response<KategoriModel> response) {
                        LoadingDialog.close();
                        if (response.isSuccessful()){
                            SuccessDialog.message(MasterKategoriActivity.this, getString(R.string.deleted_success), bind.getRoot());

                        } else {
                            ErrorDialog.message(MasterKategoriActivity.this, getString(R.string.deleted_error), bind.getRoot());
                        }

                        fetchData();
                    }

                    @Override
                    public void onFailure(Call<KategoriModel> call, Throwable t) {
                        LoadingDialog.close();
                        Toast.makeText(MasterKategoriActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
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