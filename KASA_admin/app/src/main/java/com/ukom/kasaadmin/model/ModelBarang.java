package com.ukom.kasaadmin.model;

public class ModelBarang {
    public String codeBarang, nameBarang,satuan;
    public int price, stok;

    public ModelBarang(String codeBarang, String nameBarang, String satuan, int price, int stok) {
        this.codeBarang = codeBarang;
        this.nameBarang = nameBarang;
        this.satuan = satuan;
        this.price = price;
        this.stok = stok;
    }

    public String getCodeBarang() {
        return codeBarang;
    }

    public void setCodeBarang(String codeBarang) {
        this.codeBarang = codeBarang;
    }

    public String getNameBarang() {
        return nameBarang;
    }

    public void setNameBarang(String nameBarang) {
        this.nameBarang = nameBarang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}
