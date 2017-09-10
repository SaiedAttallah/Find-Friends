package com.cosmos.saiedattallah.findfriends;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cosmos.saiedattallah.findfriends.adapters.FriendsListAdapter;
import com.cosmos.saiedattallah.findfriends.injection.BaseActivity;
import com.cosmos.saiedattallah.findfriends.models.Friend;
import com.cosmos.saiedattallah.findfriends.providers.FriendsProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FriendsListActivity extends BaseActivity implements FriendsProvider.OnRetrieveFriendsListListener {
    @Inject
    FriendsProvider friendsProvider;

    Toolbar toolbar;
    FriendsListAdapter friendsListAdapter;
    ArrayList<Friend> friendsList;
    RecyclerView rvFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        friendsList = new ArrayList<>();
        friendsProvider.setOnRetrieveFriendsListListener(this);
        friendsProvider.retrieveFriendsList();

        rvFriends = (RecyclerView) findViewById(R.id.rv_friends);
    }

    @Override
    public void onFriendsListRetrieved(List<Friend> friendsList) {
        friendsListAdapter = new FriendsListAdapter(this, friendsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFriends.setLayoutManager(layoutManager);

        // Attach the adapter to the recycler view to populate items
        rvFriends.setAdapter(friendsListAdapter);
        // Set layout manager to position the items
        friendsListAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

            }
        });
    }

    @Override
    public void onRetrieveFriendsListFailure(int failureReason, int failureMessage) {

    }
}
