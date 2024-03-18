package com.ukom.kasaadmin.ui.pengaturan.event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ukom.kasaadmin.databinding.ActivityAddEventBinding;

public class AddEventActivity extends AppCompatActivity {
    ActivityAddEventBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
    }
}