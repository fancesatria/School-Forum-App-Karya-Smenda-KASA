package com.ukom.kasa_operator.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ukom.kasa_operator.ui.karya.MainActivity;
import com.ukom.kasa_operator.component.ErrorDialog;
import com.ukom.kasa_operator.component.LoadingDialog;
import com.ukom.kasa_operator.component.SuccessDialog;
import com.ukom.kasa_operator.databinding.ActivityLoginBinding;
import com.ukom.kasa_operator.helper.Api;
import com.ukom.kasa_operator.helper.SPHelper;
import com.ukom.kasa_operator.model.LoginModel;
import com.ukom.kasa_operator.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

    }

    public void login(View view){
        if (validasi()){

        } else {
            LoginModel loginModel = new LoginModel(bind.username.getText().toString(), bind.password.getText().toString());
            prosesLogin(loginModel);
        }
    }

    public void prosesLogin(LoginModel loginModel){
        SPHelper sp = new SPHelper(LoginActivity.this);
        LoadingDialog.load(LoginActivity.this);
        Call<LoginResponse> loginResponseCall = Api.getRetrofit(LoginActivity.this).loginOperator(loginModel);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoadingDialog.close();
                if (response.isSuccessful() && response.code() == 200){
                    //simpan token dan username
                    sp.setToken(response.body().getToken());
                    sp.setEmail(response.body().getData().getEmail());
                    sp.setUsername(response.body().getData().getUsername());
                    sp.setIdPengguna(response.body().getData().getId());
                    SuccessDialog.message(LoginActivity.this, "Login berhasil", bind.getRoot());

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                } else {
                    ErrorDialog.message(LoginActivity.this, "Akun tidak ditemukan, periksa kembali password anda", bind.getRoot());
                }
                //Toast.makeText(LoginActivity.this, String.valueOf(response), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void register(View view){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }


    public boolean validasi(){
        EditText Username = bind.username;
        EditText Password = bind.password;
        if(Username.getText().toString().isEmpty() || Password.getText().toString().isEmpty()) {
            Username.requestFocus();
            Username.setError("Harap diisi");
            Password.requestFocus();
            Password.setError("Harap diisi");
            return true;
        } else {
            SuccessDialog.message(LoginActivity.this, "Login berhasil", bind.getRoot());
        }
        return false;
    }
}