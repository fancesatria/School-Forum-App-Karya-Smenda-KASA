package com.ukom.kasapengguna.ui.pengaturan.event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ukom.kasapengguna.databinding.ActivityDetailEventBinding;
import com.ukom.kasapengguna.helper.SPHelper;

public class DetailEventActivity extends AppCompatActivity {
    ActivityDetailEventBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityDetailEventBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        load();
    }

    public void load(){
        SPHelper sp = new SPHelper(this);
        bind.judulEvent.setText(getIntent().getStringExtra("judul"));
        bind.deskripsi.setText(getIntent().getStringExtra("keterangan"));
    }


}