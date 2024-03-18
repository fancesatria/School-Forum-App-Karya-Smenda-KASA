package com.ukom.kasaadmin.ui.pengaturan.auth;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ukom.kasaadmin.MainActivity;
import com.ukom.kasaadmin.R;
import com.ukom.kasaadmin.component.ErrorDialog;
import com.ukom.kasaadmin.component.LoadingDialog;
import com.ukom.kasaadmin.component.SuccessDialog;
import com.ukom.kasaadmin.databinding.ActivityRegisterBinding;
import com.ukom.kasaadmin.helper.Api;
import com.ukom.kasaadmin.helper.SPHelper;
import com.ukom.kasaadmin.model.RegisterModel;
import com.ukom.kasaadmin.response.RegisterResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding bind;
    EditText etEmail, etPassword, etConfirm, etUsername, etTelp;

    String SiteKey = "6LfIS_MkAAAAAEh-F7xq2g2Ts1eOUrNUhatmrGsD";
    String SecretKey = "6LfIS_MkAAAAAEWGdPhfJq8wToGOOqnzbs_cU9w6";
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.GoogleCaptcha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Catcha();
            }
        });
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
        Call<RegisterResponse> registerResponseCall = Api.getRetrofit().registerAdmin(registerModel);
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