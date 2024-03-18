package com.ukom.kasaadmin.response;

import com.ukom.kasaadmin.model.KategoriModel;

import java.util.ArrayList;
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
