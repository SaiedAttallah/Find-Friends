package com.cosmos.saiedattallah.findfriends.rest;

import com.cosmos.saiedattallah.findfriends.models.Friend;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WebApiInterface {
    @GET
    Call<ServerResponse<List<Friend>>> getFriendsList(@Url String url);
}
