package com.ukom.kasa_operator.ui.karya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.ukom.kasa_operator.R;
import com.ukom.kasa_operator.databinding.ActivityDetailKaryaBinding;
import com.ukom.kasa_operator.helper.SPHelper;

public class DetailKaryaActivity extends AppCompatActivity {
    ActivityDetailKaryaBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityDetailKaryaBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        load();
    }


    public void load(){
        SPHelper sp = new SPHelper(DetailKaryaActivity.this);
        bind.judulKarya.setText(getIntent().getStringExtra("judul"));
        Glide.with(DetailKaryaActivity.this).load(getIntent().getStringExtra("gambar")).into(bind.imageView1);
        bind.deskripsi.setText(getIntent().getStringExtra("keterangan"));
        bind.pencipta.setText(sp.getUsername());
    }
}