package com.cosmos.saiedattallah.findfriends.providers;

import com.cosmos.saiedattallah.findfriends.R;
import com.cosmos.saiedattallah.findfriends.injection.BaseComponent;
import com.cosmos.saiedattallah.findfriends.models.Friend;
import com.cosmos.saiedattallah.findfriends.rest.ServerResponse;
import com.cosmos.saiedattallah.findfriends.rest.WebApiInterface;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsProvider extends BaseComponent {
    @Inject
    protected WebApiInterface webApiInterface;

    public OnRetrieveFriendsListListener onRetrieveFriendsListListener;
    private List<Friend> friendsList;

    protected Callback<ServerResponse<List<Friend>>> getFriendsListCallback = new Callback<ServerResponse<List<Friend>>>() {
        @Override
        public void onResponse(Call<ServerResponse<List<Friend>>> call, Response<ServerResponse<List<Friend>>> response) {
            FriendsProvider.this.onRetrieveFriendsListCallbackResponse(call, response);
        }

        @Override
        public void onFailure(Call<ServerResponse<List<Friend>>> call, Throwable t) {
            FriendsProvider.this.onRetrieveFriendsListCallbackFailure(call, t);
        }
    };

    public FriendsProvider() {
        super();
    }

    public void setOnRetrieveFriendsListListener(OnRetrieveFriendsListListener onRetrieveFriendsListListener) {
        this.onRetrieveFriendsListListener = onRetrieveFriendsListListener;

    }

    public void removeOnRetrieveFriendsListListener(OnRetrieveFriendsListListener onRetrieveFriendsListListener) {
        this.onRetrieveFriendsListListener = null;
    }

    public List<Friend> getFriendsList() {
        return this.friendsList;
    }

    public void retrieveFriendsList() {
            Call<ServerResponse<List<Friend>>> call = this.webApiInterface.getFriendsList("https://www.digi-worx.com/friends.txt");
            call.enqueue(this.getFriendsListCallback);
    }

    private void onRetrieveFriendsListCallbackResponse(Call<ServerResponse<List<Friend>>> call, Response<ServerResponse<List<Friend>>> response) {
        ServerResponse<List<Friend>> serverResponse;

        if (response.isSuccessful()) {
            serverResponse = response.body();
            if (serverResponse.getStatus() == ServerResponse.RESPONSE_STATUS_SUCCESS) {
                this.friendsList = serverResponse.getFriends();
                if (this.onRetrieveFriendsListListener != null) {
                    this.onRetrieveFriendsListListener.onFriendsListRetrieved(this.friendsList);
                }
            }
        }


        if (this.friendsList == null) {
            this.onRetrieveFriendsListListener.onRetrieveFriendsListFailure(R.string.find_friends_error, R.string.find_friends_error_msg);
        }

    }

    private void onRetrieveFriendsListCallbackFailure(Call<ServerResponse<List<Friend>>> call, Throwable t) {
        this.onRetrieveFriendsListListener.onRetrieveFriendsListFailure(R.string.find_friends_error, R.string.find_friends_error_msg);


    }

    public interface OnRetrieveFriendsListListener {
        void onFriendsListRetrieved(List<Friend> friendsList);

        void onRetrieveFriendsListFailure(int failureReason, int failureMessage);
    }
}
