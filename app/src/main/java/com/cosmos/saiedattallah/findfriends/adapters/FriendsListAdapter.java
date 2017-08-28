package com.cosmos.saiedattallah.findfriends.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cosmos.saiedattallah.findfriends.R;
import com.cosmos.saiedattallah.findfriends.models.Friend;

import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {
    private List<Friend> friendsList;
    private Context mContext;
    public FriendsListAdapter(Context context, List<Friend> friendsList) {
        this.friendsList = friendsList;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }
    private static OnItemClickListener listener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View friendView = inflater.inflate(R.layout.item_friend, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(friendView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
// Get the data model based on position
        Friend friend = this.friendsList.get(position);

        // Set item views based on your views and data model
        TextView nameTextView = viewHolder.tvFirstName;
        nameTextView.setText(friend.getFirstName());
    }

    @Override
    public int getItemCount() {
        return this.friendsList.size();
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFirstName;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.tvFirstName = (TextView) itemView.findViewById(R.id.tv_first_name);

            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}
