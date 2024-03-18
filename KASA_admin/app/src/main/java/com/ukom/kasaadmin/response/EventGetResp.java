package com.ukom.kasaadmin.response;

import com.ukom.kasaadmin.model.EventModel;

import java.util.List;

public class EventGetResp {

    private List<EventModel> data;

    public EventGetResp(List<EventModel> data) {
        this.data = data;
    }

    public List<EventModel> getData() {
        return data;
    }

    public void setData(List<EventModel> data) {
        this.data = data;
    }
}
