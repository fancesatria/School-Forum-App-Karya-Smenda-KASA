package com.ukom.kasapengguna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.ukom.kasapengguna.databinding.ActivityMainBinding;
import com.ukom.kasapengguna.ui.pengaturan.event.EventFragment;
import com.ukom.kasapengguna.ui.pengaturan.home.HomeFragment;
import com.ukom.kasapengguna.ui.pengaturan.karya.KaryaFragment;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding bind;
    private MeowBottomNavigation meowBottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        getSupportActionBar().hide();
        meow();

    }

    public void meow(){
        meowBottomNavigation = bind.meownav;

        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_menu_book_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_home_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_newspaper_24));

        meowBottomNavigation.show(2, true);
        meowBottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()){
                    case 1 :
                        replace(new KaryaFragment());
                        break;
                    case 2:
                        replace(new HomeFragment());
                        break;
                    case 3 :
                        replace(new EventFragment());
                        break;
                }
                return null;
            }
        });
    }

    private void replace(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(Integer.valueOf(R.id.framelayout), fragment).commit();
    }




}