package com.ukom.kasaadmin.ui.pengaturan.event;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ukom.kasaadmin.R;
import com.ukom.kasaadmin.component.ErrorDialog;
import com.ukom.kasaadmin.component.LoadingDialog;
import com.ukom.kasaadmin.component.SuccessDialog;
import com.ukom.kasaadmin.databinding.ActivityDetailEventBinding;
import com.ukom.kasaadmin.helper.Api;
import com.ukom.kasaadmin.helper.SPHelper;
import com.ukom.kasaadmin.model.EventModel;
import com.ukom.kasaadmin.ui.pengaturan.MasterEventActivity;
import com.ukom.kasaadmin.ui.pengaturan.MasterKategoriActivity;
import com.ukom.kasaadmin.ui.pengaturan.karya.DetailKaryaActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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