package com.cosmos.saiedattallah.findfriends.providers;

import com.cosmos.saiedattallah.findfriends.models.Friend;
import com.cosmos.saiedattallah.findfriends.rest.WebApiInterface;

import java.util.List;

import javax.inject.Inject;

public class FriendsProvider {
    @Inject
    protected WebApiInterface webApiInterface;

    public OnRetrieveFriendsListListener onRetrieveFriendsListListener;
    private List<Friend> friendsList;

    public interface OnRetrieveFriendsListListener {
        void onFriendsListRetrieved(List<Friend> friendsList);

        void onRetrieveFriendsListFailure(int failureReason, int failureMessage);
    }
}
