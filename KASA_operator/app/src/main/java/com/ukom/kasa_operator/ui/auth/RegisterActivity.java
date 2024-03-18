package com.ukom.kasa_operator.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ukom.kasa_operator.ui.karya.MainActivity;
import com.ukom.kasa_operator.R;
import com.ukom.kasa_operator.component.ErrorDialog;
import com.ukom.kasa_operator.component.LoadingDialog;
import com.ukom.kasa_operator.component.SuccessDialog;
import com.ukom.kasa_operator.databinding.ActivityRegisterBinding;
import com.ukom.kasa_operator.helper.Api;
import com.ukom.kasa_operator.helper.SPHelper;
import com.ukom.kasa_operator.model.RegisterModel;
import com.ukom.kasa_operator.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding bind;
    EditText etEmail, etPassword, etConfirm, etUsername, etTelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
    }

    public void buatAkun(View view) {
        if (validasi()){

        } else {
            RegisterModel registerModel = new RegisterModel();
            registerModel.setEmail(bind.email.getText().toString());
            registerModel.setUsername(bind.username.getText().toString());
            registerModel.setTelp(bind.telp.getText().toString());
            registerModel.setPassword(bind.password.getText().toString());

            prosesBuatAkun(registerModel);
        }
    }

    public void prosesBuatAkun(RegisterModel registerModel){
        LoadingDialog.load(RegisterActivity.this);
        SPHelper sp = new SPHelper(RegisterActivity.this);
        Call<RegisterResponse> registerResponseCall = Api.getRetrofit().registerOperator(registerModel);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                LoadingDialog.close();
                if (response.isSuccessful()){
                    //simpan token dan username
                    sp.setToken(response.body().getToken());

                    SuccessDialog.message(RegisterActivity.this, "Akun berhasil dibuat", bind.getRoot());
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();

                } else {
                    ErrorDialog.message(RegisterActivity.this, "Tidak dapat membuat akun", bind.getRoot());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validasi() {
        etEmail = bind.email;
        etPassword = bind.password;
        etConfirm = bind.confirmpassword;
        etUsername = bind.username;
        etTelp = bind.telp;
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
        if(etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty() || etConfirm.getText().toString().isEmpty() || etUsername.getText().toString().isEmpty() || etTelp.getText().toString().isEmpty()) {
            etEmail.requestFocus();
            etEmail.setError("Harap diisi");

            etUsername.requestFocus();
            etUsername.setError("Harap diisi");

            etTelp.requestFocus();
            etTelp.setError("Harap diisi");

            etConfirm.requestFocus();
            etConfirm.setError("Harap diisi");
            return true;

        } else if (!etPassword.getText().toString().matches(etConfirm.getText().toString())){
            ErrorDialog.message(RegisterActivity.this, getString(R.string.unmatch), bind.getRoot());
            return true;
        } else if (!etEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            ErrorDialog.message(RegisterActivity.this, getString(R.string.is_email), bind.getRoot());
            return true;
        } else if(!etPassword.getText().toString().matches(regex) || etPassword.getText().toString().length() < 8 || !etConfirm.getText().toString().matches(regex) || etPassword.getText().toString().length() < 8){
            ErrorDialog.message(RegisterActivity.this, "Password harus terdisi dari minimal 8 digit, angka, dan karakter", bind.getRoot());
            return true;
        } else {
            Toast.makeText(this, "Harap tunggu...", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}