package com.ukom.kasaadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ukom.kasaadmin.databinding.ItemKategoriBinding;
import com.ukom.kasaadmin.model.KategoriModel;
import com.ukom.kasaadmin.model.ModelBarang;
import com.ukom.kasaadmin.ui.pengaturan.MasterKategoriActivity;

import java.util.List;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.ViewHolder>{

    Context context;
    private List<KategoriModel> data;

    //constructor in nnti dipanggil di activity, perhatikan bentuk data yg direquest
    public KategoriAdapter(Context context, List<KategoriModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public KategoriAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKategoriBinding bind;
        bind = ItemKategoriBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriAdapter.ViewHolder holder, int position) {
        KategoriModel kategori = data.get(position);
        //ModelBarang kategori = data.get(position);
        holder.bind.txtKategori.setText(kategori.getKategori());
        holder.bind.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MasterKategoriActivity) context).dialogEditKategori(kategori.getId(), kategori);
            }
        });
        holder.bind.txtHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MasterKategoriActivity) context).dialogHapusKategori(kategori.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {ItemKategoriBinding bind;

        public ViewHolder(@NonNull ItemKategoriBinding itemView) {
            super(itemView.getRoot());

            bind = itemView;
        }
    }
}
