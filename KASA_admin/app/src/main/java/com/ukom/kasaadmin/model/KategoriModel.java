package com.ukom.kasaadmin.model;

public class KategoriModel {

    private int id;
    private String kategori;

    public KategoriModel(String kategori) {
        //this.id = id;
        this.kategori = kategori;
    }
    public KategoriModel() {
        this.id = id;
        this.kategori = kategori;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
