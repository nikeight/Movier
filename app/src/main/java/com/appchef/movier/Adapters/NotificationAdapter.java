package com.appchef.movier.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appchef.movier.HomeModel.Users;
import com.appchef.movier.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    List<Users> notificationList;
    Context mContext;

    public NotificationAdapter(List<Users> notificationList, Context mContext) {
        this.notificationList = notificationList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_request_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Model Class
        Users users = notificationList.get(position);

        Toast.makeText(mContext, "yah tk agaye bhaiye", Toast.LENGTH_SHORT).show();

        // On Click events.
        holder.acceptIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Accpeted Invite You both can chat now", Toast.LENGTH_SHORT).show();
                ;
            }
        });

        holder.rejectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Rejected! Noice", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting data.
        holder.usernameTv.setText(users.getUsername());
        holder.genderTv.setText(users.getGender());

        // Setting Images
        Glide.with(mContext).load(users.getImageUrl()).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final private ImageView profileImage, acceptIv, rejectIv;
        final private TextView usernameTv, genderTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.notificationProfileImageView);
            acceptIv = itemView.findViewById(R.id.acceptRequestIV);
            rejectIv = itemView.findViewById(R.id.rejectRequestIv);

            usernameTv = itemView.findViewById(R.id.notificationUserNameTv);
            genderTv = itemView.findViewById(R.id.notificationGenderTv);

        }
    }
}
