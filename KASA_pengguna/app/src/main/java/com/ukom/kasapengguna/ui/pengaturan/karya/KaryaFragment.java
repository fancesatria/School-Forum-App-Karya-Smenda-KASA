package com.ukom.kasapengguna.ui.pengaturan.karya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ukom.kasapengguna.AboutUsActivity;
import com.ukom.kasapengguna.PrivacyPolicyActivity;
import com.ukom.kasapengguna.R;
import com.ukom.kasapengguna.SearchKaryaActivity;
import com.ukom.kasapengguna.TermAndConditions;
import com.ukom.kasapengguna.adapter.KaryaAdapter;
import com.ukom.kasapengguna.component.ErrorDialog;
import com.ukom.kasapengguna.databinding.DialogNavHeaderBinding;
import com.ukom.kasapengguna.databinding.FragmentKaryaBinding;
import com.ukom.kasapengguna.helper.Api;
import com.ukom.kasapengguna.helper.SPHelper;
import com.ukom.kasapengguna.model.KaryaModel;
import com.ukom.kasapengguna.ui.pengaturan.KaryaSayaActivity;
import com.ukom.kasapengguna.ui.pengaturan.ProfileActivity;
import com.ukom.kasapengguna.ui.pengaturan.auth.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KaryaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KaryaFragment extends Fragment {
    FragmentKaryaBinding bind;
//    CompositeDisposable compositeDisposable = new CompositeDisposable();
//    Service service;
    List<KaryaModel> data = new ArrayList<>();
    KaryaAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static KaryaFragment instance = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public KaryaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KaryaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KaryaFragment newInstance(String param1, String param2) {
        KaryaFragment fragment = new KaryaFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = FragmentKaryaBinding.inflate(inflater, container, false);
//        bind.item.setLayoutManager(new LinearLayoutManager(getContext()));
        load();
        instance = this;
        return bind.getRoot();
    }

    public static KaryaFragment getInstance() {
        return instance;
    }

    public void load(){
        menuheader();
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
                        adapter = new KaryaAdapter(getContext(), data);
                        bind.item.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    ErrorDialog.message(getContext(), getString(R.string.cant_access), bind.getRoot());
                    //Toast.makeText(getContext(), response.body().getJudul(), Toast.LENGTH_SHORT).show();
                }
//                data = response.body();
//                bind.item.setLayoutManager(new LinearLayoutManager(getContext()));
//                adapter = new KaryaAdapter(getContext(), data);
//                bind.item.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<KaryaModel>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}