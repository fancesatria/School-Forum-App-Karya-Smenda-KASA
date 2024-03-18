package com.ukom.kasaadmin.ui.pengaturan.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.ukom.kasaadmin.AboutUsActivity;
import com.ukom.kasaadmin.PrivacyPolicyActivity;
import com.ukom.kasaadmin.R;
import com.ukom.kasaadmin.SearchKaryaActivity;
import com.ukom.kasaadmin.TermAndConditions;
import com.ukom.kasaadmin.adapter.KaryaAdapter;
import com.ukom.kasaadmin.component.ErrorDialog;
import com.ukom.kasaadmin.helper.Api;
import com.ukom.kasaadmin.model.KaryaModel;
import com.ukom.kasaadmin.ui.pengaturan.auth.LoginActivity;
import com.ukom.kasaadmin.databinding.DialogNavHeaderBinding;
import com.ukom.kasaadmin.helper.SPHelper;
import com.ukom.kasaadmin.ui.pengaturan.KaryaSayaActivity;
import com.ukom.kasaadmin.ui.pengaturan.ProfileActivity;
import com.ukom.kasaadmin.adapter.SliderAdapter;
import com.ukom.kasaadmin.databinding.FragmentHomeBinding;
import com.ukom.kasaadmin.model.SliderItem;
import com.ukom.kasaadmin.ui.pengaturan.MasterEventActivity;
import com.ukom.kasaadmin.ui.pengaturan.MasterKategoriActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    FragmentHomeBinding bind;
    List<KaryaModel> data = new ArrayList<>();
    KaryaAdapter karyaAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SliderView sliderView;
    private SliderAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            slider();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = FragmentHomeBinding.inflate(inflater, container, false);

        load();

        return bind.getRoot();

    }

    public void load(){
        menuheader();
        slider();
        fetchData();
    }

    public void menuheader(){
        SPHelper sp = new SPHelper(getContext());
        bind.menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNavHeaderBinding binder = DialogNavHeaderBinding.inflate(LayoutInflater.from(getContext()));
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setView(binder.getRoot());
                AlertDialog dialog = alertBuilder.create();
                binder.btnProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), ProfileActivity.class));
                    }
                });

                binder.btnEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), MasterEventActivity.class));
                    }
                });

                binder.btnKategori.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), MasterKategoriActivity.class));
                    }
                });

                binder.btnKaryaSaya.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), KaryaSayaActivity.class));
                    }
                });

                binder.btnSnk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), TermAndConditions.class));
                    }
                });

                binder.btnPrivacy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), PrivacyPolicyActivity.class));
                    }
                });

                binder.btnAboutus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), AboutUsActivity.class));
                    }
                });

                binder.btnLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Konfirmasi").setMessage(R.string.logout_ensure)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sp.clearData();

                                        startActivity(new Intent(getContext(), LoginActivity.class));
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();

                    }
                });
                dialog.show();
            }
        });

        bind.searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchKaryaActivity.class));
            }
        });
    }

    public void slider(){
        sliderView = bind.imageSlider;
        adapter = new SliderAdapter(getContext());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        SliderItem sliderItem = new SliderItem();
        sliderItem.setImageUrl("https://images.app.goo.gl/xJAemdGJFYV4FFWJ8");


//        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
//            @Override
//            public void onIndicatorClicked(int position) {
//                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
//            }
//        });

        renewItems();
        removeLastItem();
    }

    public void renewItems() {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 5; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Selamat Datang di Karya Smenda");
            if (i % 2 == 0) {
                sliderItem.setImageUrl("https://www.smkn2buduran.sch.id/wp-content/uploads/2019/05/IMG_0967a-768x512.jpg");

            } else {
                sliderItem.setImageUrl("https://radarjatim.id/wp-content/uploads/2022/06/WhatsApp-Image-2022-06-06-at-09.14.28.jpeg");
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    public void removeLastItem() {
        if (adapter.getCount() - 1 >= 0)
            adapter.deleteItem(adapter.getCount() - 1);
    }

    public void fetchData(){

        Call<List<KaryaModel>> karyaGetRespCall = Api.getRetrofit(getContext()).getDataKarya();
        karyaGetRespCall.enqueue(new Callback<List<KaryaModel>>() {
            @Override
            public void onResponse(Call<List<KaryaModel>> call, Response<List<KaryaModel>> response) {
                if(response.isSuccessful()){

                    if (response.body().size() == 0 || response.body()==null){
                        Toast.makeText(getContext(), getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                    } else {
                        data = response.body();
                        bind.item.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        karyaAdapter = new KaryaAdapter(getContext(), data);
                        bind.item.setAdapter(karyaAdapter);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    ErrorDialog.message(getContext(), getString(R.string.cant_access), bind.getRoot());
                    //Toast.makeText(getContext(), response.body().getJudul(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<KaryaModel>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}