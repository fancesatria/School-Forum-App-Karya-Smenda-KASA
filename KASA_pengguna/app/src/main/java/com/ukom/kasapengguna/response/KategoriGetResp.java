package com.ukom.kasapengguna.response;

import com.ukom.kasapengguna.model.KategoriModel;

import java.util.List;

public class KategoriGetResp {
    private List<KategoriModel> data;

    public KategoriGetResp(List<KategoriModel> data) {
        this.data = data;
    }

    public List<KategoriModel> getData() {
        return data;
    }

    public void setData(List<KategoriModel> data) {
        this.data = data;
    }
}
