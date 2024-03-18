package com.ukom.kasapengguna.ui.pengaturan.auth;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ukom.kasapengguna.MainActivity;
import com.ukom.kasapengguna.component.ErrorDialog;
import com.ukom.kasapengguna.component.LoadingDialog;
import com.ukom.kasapengguna.component.SuccessDialog;
import com.ukom.kasapengguna.databinding.ActivityLoginBinding;
import com.ukom.kasapengguna.helper.Api;
import com.ukom.kasapengguna.helper.SPHelper;
import com.ukom.kasapengguna.model.LoginModel;
import com.ukom.kasapengguna.response.LoginResponse;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding bind;

    String SiteKey = "6Lc2y_MkAAAAAPvnmLyh5o2LcDHZGz8oRFM5ng5k";
    String SecretKey = "6Lc3y_MkAAAAAKlubyx7Vg4hoJabxJSB2--QnVsW";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.GoogleCaptcha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Catcha();
            }
        });

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
        Call<LoginResponse> loginResponseCall = Api.getRetrofit(LoginActivity.this).login(loginModel);
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

    public void forgotPassword(View view){
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
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

    public void Catcha(){
        SafetyNet.getClient(this).verifyWithRecaptcha(SiteKey)
                .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                        // Indicates communication with reCAPTCHA service was
                        // successful.
                        String userResponseToken = recaptchaTokenResponse.getTokenResult();
                        if (!userResponseToken.isEmpty()) {
                            handleCaptchaResult(userResponseToken);
                            // Validate the user response token using the
                            // reCAPTCHA siteverify API.
                            Log.e(TAG, "VALIDATION STEP NEEDED");
                        }else {
                            bind.GoogleCaptcha.setChecked(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            // An error occurred when communicating with the
                            // reCAPTCHA service. Refer to the status code to
                            // handle the error appropriately.
                            bind.GoogleCaptcha.setChecked(false);
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.e(TAG, "Error: " + CommonStatusCodes
                                    .getStatusCodeString(statusCode));
                        } else {
                            // A different, unknown type of error occurred.
                            Log.e(TAG, "Error: " + e.getMessage());
                        }
                    }
                });
    }

    void handleCaptchaResult(final String responseToken) {
        String url = "https://www.google.com/recaptcha/api/siteverify";
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("success")){
                                bind.GoogleCaptchaText.setText("You're not a Robot");
                                bind.GoogleCaptcha.setChecked(true);
                            }
                        } catch (Exception ex) {
                            Log.d(TAG, "Error message: " + ex.getMessage());
                            bind.GoogleCaptcha.setChecked(false);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        bind.GoogleCaptcha.setChecked(false);
                        Log.d(TAG, "Error message: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("secret", SecretKey);
                params.put("response", responseToken);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}