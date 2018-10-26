package com.example.standard.firebasestructure.model.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.standard.firebasestructure.*;
import com.example.standard.firebasestructure.R;
import com.example.standard.firebasestructure.model.User;
import com.example.standard.firebasestructure.view.OnItemClickListener;
import com.google.android.gms.tasks.*;
import com.google.firebase.storage.*;
import com.squareup.picasso.*;

import java.util.List;

public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.FriendViewHolder> {

    private List<User> friendList;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private Context context;


    public FriendRecyclerAdapter(Context context, List<User> friendList, OnItemClickListener onItemClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.friendList = friendList;
        this.onItemClickListener = onItemClickListener;
    }

    public User getItem(int id){
        return friendList.get(id);
    }

    @NonNull
    @Override
    public FriendRecyclerAdapter.FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.view_friend_item, viewGroup, false);
        return new FriendRecyclerAdapter.FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder friendViewHolder, int i) {
        friendViewHolder.bind(friendList.get(i), onItemClickListener);

        Constants.STORAGE_REF.child("images").child(friendList.get(i).getUserId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .apply(RequestOptions.centerCropTransform())
                        .apply(RequestOptions.placeholderOf(R.drawable.common_full_open_on_phone))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                        .thumbnail(0.01f)
                        .into(friendViewHolder.imageViewFriendPhoto);
            }
        });

        friendViewHolder.textViewFriendName.setText(friendList.get(i).getUserName());
        if(friendList.get(i).getDestinations() != null){
            if(friendList.get(i).getDestinations().size() == 1){
                friendViewHolder.textViewFriendDestinations.setText(
                        friendList.get(i).getDestinations().size() + context.getString(R.string.destination));
            } else {
                friendViewHolder.textViewFriendDestinations.setText(
                        friendList.get(i).getDestinations().size() + context.getString(R.string.destinations));
            }
        } else {
            friendViewHolder.textViewFriendDestinations.setText(R.string.noDestinations);
        }
    }

    @Override
    public int getItemCount() {
        if(friendList != null){
            return friendList.size();
        } else {
            return 0;
        }
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageViewFriendPhoto;
        private TextView textViewFriendName;
        private TextView textViewFriendDestinations;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFriendPhoto = itemView.findViewById(R.id.imageViewFriend);
            textViewFriendName = itemView.findViewById(R.id.friendName);
            textViewFriendDestinations = itemView.findViewById(R.id.destinations);
        }

        public void bind(User friend, OnItemClickListener onItemClickListener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(friend);
                }
            });
        }
    }
}
