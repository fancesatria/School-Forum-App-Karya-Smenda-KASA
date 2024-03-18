package com.ukom.kasa_operator.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ukom.kasa_operator.databinding.ItemKaryaBinding;
import com.ukom.kasa_operator.model.KaryaModel;
import com.ukom.kasa_operator.ui.karya.DetailKaryaActivity;

import java.util.List;

public class DitolakAdapter extends RecyclerView.Adapter<DitolakAdapter.ViewHolder>{
    Context context;
    private List<KaryaModel> data;

    public DitolakAdapter(Context context, List<KaryaModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public DitolakAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKaryaBinding bind;
        bind = ItemKaryaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull DitolakAdapter.ViewHolder holder, int position) {
        KaryaModel karya = data.get(position);
        Glide.with(context).load(karya.getGambar()).into(holder.bind.imgKarya);
        holder.bind.judulKarya.setText(karya.getJudul());
        holder.bind.tanggal.setText("Ditolak pada : "+karya.getTgl_tolak());
        holder.bind.layoutButton.setVisibility(View.GONE);
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
