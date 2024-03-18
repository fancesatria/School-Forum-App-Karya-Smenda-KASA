package com.ukom.kasapengguna.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ukom.kasapengguna.databinding.ActivityDetailEventBinding;
import com.ukom.kasapengguna.databinding.ItemEventBinding;
import com.ukom.kasapengguna.model.EventModel;
import com.ukom.kasapengguna.ui.pengaturan.event.EventFragment;

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
