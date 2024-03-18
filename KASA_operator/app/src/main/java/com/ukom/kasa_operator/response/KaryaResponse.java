package com.ukom.kasa_operator.response;


import com.ukom.kasa_operator.model.KaryaModel;

import java.util.List;

public class KaryaResponse {
    private String pesan;
    private int status;
    private List<KaryaModel> data;

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

    public List<KaryaModel> getData() {
        return data;
    }

    public void setData(List<KaryaModel> data) {
        this.data = data;
    }
}
