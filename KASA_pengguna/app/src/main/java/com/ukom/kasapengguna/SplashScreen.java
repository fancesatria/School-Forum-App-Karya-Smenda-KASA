package com.ukom.kasapengguna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.ukom.kasapengguna.ui.pengaturan.auth.LoginActivity;
import com.ukom.kasapengguna.helper.SPHelper;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIMEOUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //pengecekan
        SPHelper sp = new SPHelper(this);
        String token = sp.getToken();

        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!token.isEmpty()){
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }

            }
        }, SPLASH_SCREEN_TIMEOUT);

        Animation fadeOut=new AlphaAnimation(1,0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(500);
        fadeOut.setDuration(1800);
        ImageView image=findViewById(R.id.imageView2);

        image.setAnimation(fadeOut);
    }
}