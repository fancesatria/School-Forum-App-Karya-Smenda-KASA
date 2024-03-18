package com.ukom.kasapengguna;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ukom.kasapengguna.component.ErrorDialog;
import com.ukom.kasapengguna.component.LoadingDialog;
import com.ukom.kasapengguna.databinding.ActivityEditProfileBinding;
import com.ukom.kasapengguna.helper.Api;

import hivatec.ir.easywebservice.EasyWebservice;
import hivatec.ir.easywebservice.Method;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding bind;
    String username, email, telp;
    int iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        load();

        bind.savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etusername = bind.username.getText().toString();
                String ettelp = bind.telp.getText().toString();
                String etemail = bind.email.getText().toString();

                if (etusername.isEmpty() || ettelp.isEmpty()){
                    ErrorDialog.message(EditProfileActivity.this, getString(R.string.empty), bind.getRoot());

                } else if (!etemail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                    ErrorDialog.message(EditProfileActivity.this, getString(R.string.is_email), bind.getRoot());
                } else {
                    new EasyWebservice(Api.BASE_URL+"edit/"+iduser)
                            .method(Method.POST) //default
                            .addParam("username", etusername) //adding params to body
                            .addParam("email", etemail) //adding params to body
                            .addParam("telp", ettelp) //adding params to body
                            .call(new hivatec.ir.easywebservice.Callback.A<String>("msg") {
                                @Override
                                public void onSuccess(String s) {
                                    LoadingDialog.close();
                                    Toast.makeText(bind.getRoot().getContext(), s.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(String s) {
                                    LoadingDialog.close();
                                    Toast.makeText(bind.getRoot().getContext(), s.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    public void load(){
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        iduser = getIntent().getIntExtra("iduser",0);

        bind.username.setText(username);
        bind.email.setText(email);
    }

}