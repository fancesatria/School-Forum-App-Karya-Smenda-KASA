package com.ukom.kasapengguna.ui.pengaturan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ukom.kasapengguna.EditProfileActivity;
import com.ukom.kasapengguna.databinding.ActivityProfileBinding;
import com.ukom.kasapengguna.helper.SPHelper;
import com.ukom.kasapengguna.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding bind;
    List<UserModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        showUser();
    }

    public void showUser(){
        SPHelper sp = new SPHelper(this);
        String username = sp.getUsername();
        String email = sp.getEmail();

        bind.username.setText(username);
        bind.email.setText(email);

        bind.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
                i.putExtra("username", sp.getUsername());
                i.putExtra("email", sp.getEmail());
                i.putExtra("iduser", sp.getIdPengguna());
                startActivity(i);
            }
        });
    }
}