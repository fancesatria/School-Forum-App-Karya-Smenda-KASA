package com.ukom.kasaadmin.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ukom.kasaadmin.model.EventModel;

public class EventResponse {

    @SerializedName("pesan")
    @Expose
    private String pesan;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private EventModel data;

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public EventModel getData() {
        return data;
    }

    public void setData(EventModel data) {
        this.data = data;
    }
}
