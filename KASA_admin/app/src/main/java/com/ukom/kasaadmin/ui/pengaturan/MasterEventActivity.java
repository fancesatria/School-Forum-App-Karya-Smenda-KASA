package com.ukom.kasaadmin.ui.pengaturan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ukom.kasaadmin.R;
import com.ukom.kasaadmin.adapter.EventAdapter;
import com.ukom.kasaadmin.component.ErrorDialog;
import com.ukom.kasaadmin.databinding.ActivityMasterEventBinding;
import com.ukom.kasaadmin.helper.Api;
import com.ukom.kasaadmin.model.EventModel;
import com.ukom.kasaadmin.ui.pengaturan.event.AddEventActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterEventActivity extends AppCompatActivity {
    ActivityMasterEventBinding bind;
    List<EventModel> data = new ArrayList<>();
    EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMasterEventBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        fetchData();
        cari();

        bind.plusBtnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MasterEventActivity.this, AddEventActivity.class));
            }
        });
    }

    public void fetchData(){
        String keyword = bind.searhview.getQuery().toString();
        Call<List<EventModel>> karyaGetRespCall = Api.getRetrofit(MasterEventActivity.this).getDataEvent(keyword);
        karyaGetRespCall.enqueue(new Callback<List<EventModel>>() {
            @Override
            public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
                if(response.isSuccessful()){
                    data = response.body();
                    bind.item.setLayoutManager(new LinearLayoutManager(MasterEventActivity.this));
                    adapter = new EventAdapter(MasterEventActivity.this, data);
                    bind.item.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    if (response.body().size() == 0){
                        Toast.makeText(MasterEventActivity.this, getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    ErrorDialog.message(MasterEventActivity.this, getString(R.string.cant_access), bind.getRoot());
                    //Toast.makeText(MasterEventActivity.this, response.body().getJudul(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<EventModel>> call, Throwable t) {
                Toast.makeText(MasterEventActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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