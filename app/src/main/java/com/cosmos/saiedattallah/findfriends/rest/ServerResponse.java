package com.cosmos.saiedattallah.findfriends.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerResponse<T> {
    public static final int RESPONSE_STATUS_SUCCESS = 1;
    public static final int RESPONSE_STATUS_FAIL = 0;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("friends")
    @Expose
    private T friends;


    public int getStatus() {
        return this.status;
    }

    public String getError() {
        return this.error;
    }

    public T getFriends() {
        return this.friends;
    }
}
