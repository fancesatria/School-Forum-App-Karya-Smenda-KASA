package com.ukom.kasapengguna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ukom.kasapengguna.adapter.KaryaAdapter;
import com.ukom.kasapengguna.component.ErrorDialog;
import com.ukom.kasapengguna.databinding.ActivitySearchKaryaBinding;
import com.ukom.kasapengguna.helper.Api;
import com.ukom.kasapengguna.model.KaryaModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchKaryaActivity extends AppCompatActivity {

    ActivitySearchKaryaBinding bind;
    List<KaryaModel> data = new ArrayList<>();
    KaryaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivitySearchKaryaBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        fetchData();
        cari();
    }

    public void fetchData(){
        String keyword = bind.searhview.getQuery().toString();
        Call<List<KaryaModel>> karyaGetRespCall = Api.getRetrofit(SearchKaryaActivity.this).getDataKarya(keyword);
        karyaGetRespCall.enqueue(new Callback<List<KaryaModel>>() {
            @Override
            public void onResponse(Call<List<KaryaModel>> call, Response<List<KaryaModel>> response) {
                if(response.isSuccessful()){


                    if (response.body().size() == 0 || response.body()==null){
                        Toast.makeText(SearchKaryaActivity.this, getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                        bind.item.setVisibility(View.GONE);
                    } else {
                        data = response.body();
                        bind.item.setLayoutManager(new GridLayoutManager(SearchKaryaActivity.this, 3));
                        adapter = new KaryaAdapter(SearchKaryaActivity.this, data);
                        bind.item.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    ErrorDialog.message(SearchKaryaActivity.this, getString(R.string.cant_access), bind.getRoot());
                    //Toast.makeText(SearchKaryaActivity.this, response.body().getJudul(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<KaryaModel>> call, Throwable t) {
                Toast.makeText(SearchKaryaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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