package com.ukom.kasaadmin.ui.pengaturan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ukom.kasaadmin.EditProfileActivity;
import com.ukom.kasaadmin.R;
import com.ukom.kasaadmin.databinding.ActivityProfileBinding;
import com.ukom.kasaadmin.helper.Api;
import com.ukom.kasaadmin.helper.SPHelper;
import com.ukom.kasaadmin.model.KategoriModel;
import com.ukom.kasaadmin.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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