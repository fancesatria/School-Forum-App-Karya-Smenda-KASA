package com.ukom.kasaadmin.ui.pengaturan.event;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ukom.kasaadmin.R;
import com.ukom.kasaadmin.SearchEventActivity;
import com.ukom.kasaadmin.TermAndConditions;
import com.ukom.kasaadmin.adapter.EventAdapter;
import com.ukom.kasaadmin.adapter.KaryaAdapter;
import com.ukom.kasaadmin.component.ErrorDialog;
import com.ukom.kasaadmin.component.LoadingDialog;
import com.ukom.kasaadmin.component.SuccessDialog;
import com.ukom.kasaadmin.databinding.ActivityAddEventBinding;
import com.ukom.kasaadmin.databinding.ActivityDetailEventBinding;
import com.ukom.kasaadmin.databinding.AddEventBinding;
import com.ukom.kasaadmin.databinding.DialogAddKategoriBinding;
import com.ukom.kasaadmin.databinding.DialogEditKategoriBinding;
import com.ukom.kasaadmin.helper.Api;
import com.ukom.kasaadmin.model.EventModel;
import com.ukom.kasaadmin.model.KaryaModel;
import com.ukom.kasaadmin.model.KategoriModel;
import com.ukom.kasaadmin.ui.pengaturan.auth.LoginActivity;
import com.ukom.kasaadmin.databinding.DialogNavHeaderBinding;
import com.ukom.kasaadmin.databinding.FragmentEventBinding;
import com.ukom.kasaadmin.helper.SPHelper;
import com.ukom.kasaadmin.ui.pengaturan.KaryaSayaActivity;
import com.ukom.kasaadmin.ui.pengaturan.ProfileActivity;
import com.ukom.kasaadmin.ui.pengaturan.MasterEventActivity;
import com.ukom.kasaadmin.ui.pengaturan.MasterKategoriActivity;
import com.ukom.kasaadmin.ui.pengaturan.karya.AddKaryaActivity;
import com.ukom.kasaadmin.ui.pengaturan.karya.KaryaFragment;

import java.util.ArrayList;
import java.util.List;

import hivatec.ir.easywebservice.EasyWebservice;
import hivatec.ir.easywebservice.Method;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    List<EventModel> data = new ArrayList<>();
    EventAdapter adapter;
    FragmentEventBinding bind;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static EventFragment instance = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
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
        bind = FragmentEventBinding.inflate(inflater, container, false);
        bind.item.setAdapter(adapter);
        load();
        instance = this;
        return bind.getRoot();
    }

    public static EventFragment getInstance() {
        return instance;
    }

    public void load(){
        menuheader();
        fetchData();
    }

    public void menuheader(){
        bind.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTambahEvent();
            }
        });

        bind.searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchEventActivity.class));
            }
        });
    }

    public void fetchData(){
        Call<List<EventModel>> karyaGetRespCall = Api.getRetrofit(getContext()).getDataEvent();
        karyaGetRespCall.enqueue(new Callback<List<EventModel>>() {
            @Override
            public void onResponse(Call<List<EventModel>> call, Response<List<EventModel>> response) {
                if(response.isSuccessful()){
                    if (response.body().size() == 0 || response.body()==null){
                        Toast.makeText(getContext(), getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                    } else {
                        data = response.body();
                        bind.item.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter = new EventAdapter(getContext(), data);
                        bind.item.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    ErrorDialog.message(getContext(), getString(R.string.cant_access), bind.getRoot());
                    //Toast.makeText(getContext(), response.body().getJudul(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<EventModel>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hapus(int idevent){
        androidx.appcompat.app.AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Konfirmasi");
        alert.setMessage(R.string.del_ensure);
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoadingDialog.load(getContext());
                Call<EventModel> kategoriModelCall = Api.getRetrofit(getContext()).delEvent(idevent);
                kategoriModelCall.enqueue(new Callback<EventModel>() {
                    @Override
                    public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                        LoadingDialog.close();
                        if (response.isSuccessful()){
                            SuccessDialog.message(getContext(), getString(R.string.deleted_success), bind.getRoot());

                        } else {
                            ErrorDialog.message(getContext(), getString(R.string.deleted_error), bind.getRoot());
                        }
                        fetchData();
                    }

                    @Override
                    public void onFailure(Call<EventModel> call, Throwable t) {
                        LoadingDialog.close();
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    public void dialogTambahEvent(){
        ActivityAddEventBinding binding = ActivityAddEventBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(binding.getRoot());
        AlertDialog  dialog = alert.create();
        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.load(bind.getRoot().getContext());
                String judul = binding.judulEvent.getText().toString();
                String deskripsi = binding.deskripsi.getText().toString();

                if (judul.isEmpty() || deskripsi.isEmpty()){
                    ErrorDialog.message(getContext(), getString(R.string.empty), bind.getRoot());
                } else {
                    EventModel ev = new EventModel();
                    ev.setJudul(judul);
                    ev.setDeskripsi(deskripsi);
                    new EasyWebservice(Api.BASE_URL+"event/add")
                            .method(Method.POST) //default
                            .addParam("judul", judul) //adding params to body
                            .addParam("deskripsi", deskripsi) //adding params to body
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
        dialog.show();
    }

    public void dialogEditEvent(int id, EventModel eventModel){
        ActivityAddEventBinding binding = ActivityAddEventBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(binding.getRoot());
        AlertDialog  dialog = alert.create();
        binding.bannerKarya.setText("Edit Event");
        binding.upload.setText("Simpan Perubahan");
        binding.judulEvent.setText(eventModel.getJudul());
        binding.deskripsi.setText(eventModel.getDeskripsi());
        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.load(bind.getRoot().getContext());
                String judul = binding.judulEvent.getText().toString();
                String deskripsi = binding.deskripsi.getText().toString();

                if (!judul.isEmpty() || !deskripsi.isEmpty()){
                    new EasyWebservice(Api.BASE_URL+"event/edit/"+id)
                            .method(Method.POST) //default
                            .addParam("judul", judul) //adding params to body
                            .addParam("deskripsi", deskripsi) //adding params to body
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


                } else {
                    ErrorDialog.message(bind.getRoot().getContext(), getString(R.string.empty), bind.getRoot());
                }
            }
        });
        dialog.show();
    }


}