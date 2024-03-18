package com.ukom.kasa_operator.response;

import com.ukom.kasa_operator.model.KaryaModel;
import com.ukom.kasa_operator.model.UbahStatusModel;

public class UbahStatusResponse {
    private String pesan;
    private UbahStatusModel data;

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public UbahStatusModel getData() {
        return data;
    }

    public void setData(UbahStatusModel data) {
        this.data = data;
    }
}
