package com.ukom.kasaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.ukom.kasaadmin.adapter.EventAdapter;
import com.ukom.kasaadmin.component.ErrorDialog;
import com.ukom.kasaadmin.databinding.ActivitySearchEventBinding;
import com.ukom.kasaadmin.helper.Api;
import com.ukom.kasaadmin.model.EventModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchEventActivity extends AppCompatActivity {

    ActivitySearchEventBinding bind;
    List<EventModel> data = new ArrayList<>();
    EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivitySearchEventBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        fetchData();
        cari();
    }

    public void fetchData(){
        String keyword = bind.searhview.getQuery().toString();
        Call<List<EventModel>> karyaGetRespCall = Api.getRetrofit(SearchEventActivity.this).getDataEvent(keyword);
        karyaGetRespCall.enqueue(new Callback<List<EventModel>>() {
            @Override
            public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
                if(response.isSuccessful()){
                    if (response.body().size() == 0 || response.body()==null){
                        Toast.makeText(SearchEventActivity.this, getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                    } else {
                        data = response.body();
                        bind.item.setLayoutManager(new LinearLayoutManager(SearchEventActivity.this));
                        adapter = new EventAdapter(SearchEventActivity.this, data);
                        bind.item.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    ErrorDialog.message(SearchEventActivity.this, getString(R.string.cant_access), bind.getRoot());
                    //Toast.makeText(SearchEventActivity.this, response.body().getJudul(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<EventModel>> call, Throwable t) {
                Toast.makeText(SearchEventActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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