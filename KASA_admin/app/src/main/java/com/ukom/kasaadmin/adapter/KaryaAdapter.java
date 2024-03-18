package com.ukom.kasaadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ukom.kasaadmin.databinding.ItemKaryaBinding;
import com.ukom.kasaadmin.databinding.ItemKategoriBinding;
import com.ukom.kasaadmin.model.KaryaModel;
import com.ukom.kasaadmin.ui.pengaturan.karya.DetailKaryaActivity;

import java.util.ArrayList;
import java.util.List;

public class KaryaAdapter extends RecyclerView.Adapter<KaryaAdapter.ViewHolder> {
    Context context;
    private List<KaryaModel> data;

    public KaryaAdapter(Context context, List<KaryaModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public KaryaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKaryaBinding bind;
        bind = ItemKaryaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull KaryaAdapter.ViewHolder holder, int position) {
        KaryaModel karya = data.get(position);
        Glide.with(context).load(karya.getGambar()).into(holder.bind.imgKarya);
        holder.bind.judulKarya.setText(karya.getJudul());
        holder.bind.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailKaryaActivity.class);
                i.putExtra("idkarya", karya.getId());
                i.putExtra("gambar", karya.getGambar());
                i.putExtra("idpengguna", karya.getIdpengguna());
                i.putExtra("idkategori", karya.getIdkategori());
                i.putExtra("judul", karya.getJudul());
                i.putExtra("keterangan", karya.getKeterangan());
                i.putExtra("tglupload", karya.getTgl_upload());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        //if(data==null) return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemKaryaBinding bind;

        public ViewHolder(@NonNull ItemKaryaBinding itemView) {
            super(itemView.getRoot());

            bind = itemView;
        }
    }
}
