package com.ukom.kasapengguna.ui.pengaturan.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ukom.kasapengguna.R;
import com.ukom.kasapengguna.component.ErrorDialog;
import com.ukom.kasapengguna.component.LoadingDialog;
import com.ukom.kasapengguna.component.SuccessDialog;
import com.ukom.kasapengguna.databinding.ActivityForgotPasswordBinding;
import com.ukom.kasapengguna.helper.Api;
import com.ukom.kasapengguna.model.RegisterModel;
import com.ukom.kasapengguna.response.ForgotPasswordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding bind;
    EditText etEmail, etPassword, etConfirm, etUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
    }

    public void ubahPassword(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Konfirmasi");
        alert.setMessage("Anda akan mengubah password akun ini? 1/2");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (validasi()){

                } else {
                    RegisterModel registerModel = new RegisterModel();
                    registerModel.setUsername(bind.username.getText().toString());
                    registerModel.setEmail(bind.email.getText().toString());
                    registerModel.setPassword(bind.password.getText().toString());

                    prosesUbahPassword(registerModel);
                }
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();

    }

    public void prosesUbahPassword(RegisterModel registerModel){
        LoadingDialog.load(ForgotPasswordActivity.this);
        Call<ForgotPasswordResponse> forgotPasswordResponseCall = Api.getRetrofit().forgotPassword(registerModel);
        forgotPasswordResponseCall.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                LoadingDialog.close();
                if (response.isSuccessful()){
                    SuccessDialog.message(ForgotPasswordActivity.this, getString(R.string.pw_success), bind.getRoot());
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                    finish();
                } else {
                    ErrorDialog.message(ForgotPasswordActivity.this, getString(R.string.pw_failed), bind.getRoot());
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(ForgotPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validasi() {
        etEmail = bind.email;
        etPassword = bind.password;
        etConfirm = bind.confirmpassword;
        etUsername = bind.username;
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
        if(etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty() || etConfirm.getText().toString().isEmpty() || etUsername.getText().toString().isEmpty()) {
            etEmail.requestFocus();
            etEmail.setError("Harap diisi");

            etUsername.requestFocus();
            etUsername.setError("Harap diisi");

            etConfirm.requestFocus();
            etConfirm.setError("Harap diisi");
            return true;

        } else if (!etPassword.getText().toString().matches(etConfirm.getText().toString())){
            ErrorDialog.message(ForgotPasswordActivity.this, getString(R.string.unmatch), bind.getRoot());
            return true;
        } else if (!etEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            ErrorDialog.message(ForgotPasswordActivity.this, getString(R.string.is_email), bind.getRoot());
            return true;
        } else if(!etPassword.getText().toString().matches(regex) || etPassword.getText().toString().length() < 8 || !etConfirm.getText().toString().matches(regex) || etPassword.getText().toString().length() < 8){
            ErrorDialog.message(ForgotPasswordActivity.this, "Password harus terdisi dari minimal 8 digit, angka, dan karakter", bind.getRoot());
            return true;
        } else {
            Toast.makeText(this, "Harap tunggu...", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}