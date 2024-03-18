package com.ukom.kasapengguna.model;

public class KaryaModel {
    private int id,idkategori, idpengguna;
    private String judul, keterangan,
            gambar, tgl_upload, tgl_verif, status, link,like;

    public KaryaModel(int id, int idkategori, int idpengguna, String judul, String keterangan, String gambar, String tgl_upload, String tgl_verif, String status, String link, String like) {
        this.id = id;
        this.idkategori = idkategori;
        this.idpengguna = idpengguna;
        this.judul = judul;
        this.keterangan = keterangan;
        this.gambar = gambar;
        this.tgl_upload = tgl_upload;
        this.tgl_verif = tgl_verif;
        this.status = status;
        this.link = link;
        this.like = like;
    }

    public KaryaModel() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(int idkategori) {
        this.idkategori = idkategori;
    }

    public int getIdpengguna() {
        return idpengguna;
    }

    public void setIdpengguna(int idpengguna) {
        this.idpengguna = idpengguna;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getTgl_upload() {
        return tgl_upload;
    }

    public void setTgl_upload(String tgl_upload) {
        this.tgl_upload = tgl_upload;
    }

    public String getTgl_verif() {
        return tgl_verif;
    }

    public void setTgl_verif(String tgl_verif) {
        this.tgl_verif = tgl_verif;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }
}
