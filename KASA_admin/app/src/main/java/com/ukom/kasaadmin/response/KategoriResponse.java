package com.ukom.kasaadmin.response;

import com.ukom.kasaadmin.model.KategoriModel;

import java.util.List;

public class KategoriResponse {
    private String pesan;
    private int status;
    private KategoriModel data;

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public KategoriModel getData() {
        return data;
    }

    public void setData(KategoriModel data) {
        this.data = data;
    }
}
