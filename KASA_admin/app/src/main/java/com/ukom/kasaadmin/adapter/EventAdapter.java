package com.ukom.kasaadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ukom.kasaadmin.databinding.ActivityDetailEventBinding;
import com.ukom.kasaadmin.databinding.ItemEventBinding;
import com.ukom.kasaadmin.databinding.ItemKaryaBinding;
import com.ukom.kasaadmin.model.EventModel;
import com.ukom.kasaadmin.model.KaryaModel;
import com.ukom.kasaadmin.ui.pengaturan.MasterKategoriActivity;
import com.ukom.kasaadmin.ui.pengaturan.event.DetailEventActivity;
import com.ukom.kasaadmin.ui.pengaturan.event.EventFragment;
import com.ukom.kasaadmin.ui.pengaturan.karya.DetailKaryaActivity;
import com.ukom.kasaadmin.ui.pengaturan.karya.KaryaFragment;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    Context context;
    private List<EventModel> data;

    public EventAdapter(Context context, List<EventModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEventBinding bind;
        bind = ItemEventBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        EventModel karya = data.get(position);
        holder.bind.judulEvent.setText(karya.getJudul());
        holder.bind.deskripsi.setText(karya.getDeskripsi());
        holder.bind.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityDetailEventBinding binding = ActivityDetailEventBinding.inflate(LayoutInflater.from(context));
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(binding.getRoot());
                AlertDialog  dialog = alert.create();
                binding.judulEvent.setText(karya.getJudul());
                binding.deskripsi.setText(karya.getDeskripsi());
                dialog.show();
            }
        });
        holder.bind.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFragment.getInstance().hapus(karya.getId());
            }
        });

        holder.bind.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFragment.getInstance().dialogEditEvent(karya.getId(), karya);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemEventBinding bind;

        public ViewHolder(@NonNull ItemEventBinding itemView) {
            super(itemView.getRoot());

            bind = itemView;
        }
    }


}
