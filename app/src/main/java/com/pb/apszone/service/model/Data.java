package com.pb.apszone.service.model;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("params")
    private Object params;

    public void setParams(Object params) {
        this.params = params;
    }

    public Object getParams() {
        return params;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "params = '" + params + '\'' +
                        "}";
    }
}