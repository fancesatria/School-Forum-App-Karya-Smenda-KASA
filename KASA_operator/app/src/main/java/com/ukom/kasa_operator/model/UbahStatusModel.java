package com.ukom.kasa_operator.model;

public class UbahStatusModel {
    public String status, tgl_verif;

    public UbahStatusModel(String status, String tgl_verif) {
        this.status = status;
        this.tgl_verif = tgl_verif;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTgl_verif() {
        return tgl_verif;
    }

    public void setTgl_verif(String tgl_verif) {
        this.tgl_verif = tgl_verif;
    }
}
