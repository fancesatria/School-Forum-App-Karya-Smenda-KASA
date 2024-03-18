package com.ukom.kasaadmin.ui.pengaturan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.ukom.kasaadmin.R;
import com.ukom.kasaadmin.adapter.KaryaAdapter;
import com.ukom.kasaadmin.component.ErrorDialog;
import com.ukom.kasaadmin.databinding.ActivityKaryaSayaBinding;
import com.ukom.kasaadmin.helper.Api;
import com.ukom.kasaadmin.helper.SPHelper;
import com.ukom.kasaadmin.model.KaryaModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryaSayaActivity extends AppCompatActivity {
    List<KaryaModel> data = new ArrayList<>();
    KaryaAdapter adapter;
    ActivityKaryaSayaBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityKaryaSayaBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        SPHelper sp = new SPHelper(KaryaSayaActivity.this);
        fetchData(sp.getIdPengguna());
    }

    public void fetchData(int id){
        Call<List<KaryaModel>> karyaGetRespCall = Api.getRetrofit(KaryaSayaActivity.this).getDataKarya(id);
        karyaGetRespCall.enqueue(new Callback<List<KaryaModel>>() {
            @Override
            public void onResponse(Call<List<KaryaModel>> call, Response<List<KaryaModel>> response) {
                if(response.isSuccessful()){
                    data = response.body();
                    bind.item.setLayoutManager(new GridLayoutManager(KaryaSayaActivity.this, 3));
                    adapter = new KaryaAdapter(KaryaSayaActivity.this, data);
                    bind.item.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    if (response.body().size() == 0){
                        Toast.makeText(KaryaSayaActivity.this, getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    ErrorDialog.message(KaryaSayaActivity.this, getString(R.string.cant_access), bind.getRoot());
                    //Toast.makeText(KaryaSayaActivity.this, response.body().getJudul(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<KaryaModel>> call, Throwable t) {
                Toast.makeText(KaryaSayaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}