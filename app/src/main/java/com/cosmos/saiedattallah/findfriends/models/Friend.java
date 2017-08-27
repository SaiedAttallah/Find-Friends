package com.cosmos.saiedattallah.findfriends.models;

import com.google.gson.annotations.SerializedName;

public class Friend {
    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String LastName;

    @SerializedName("email")
    private String email;

    @SerializedName("call")
    private String call;

    @SerializedName("id")
    private String id;

    @SerializedName("location")
    private double[] location;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }
}
