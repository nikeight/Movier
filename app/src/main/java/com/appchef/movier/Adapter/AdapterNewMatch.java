package com.appchef.movier.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appchef.movier.Model.ModelNewMatch;
import com.appchef.movier.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterNewMatch extends RecyclerView.Adapter<AdapterNewMatch.MyHolder> {

    private Context context;
    List<ModelNewMatch> newMatchList;

    public AdapterNewMatch(Context context, List<ModelNewMatch> newMatchList) {
        this.context = context;
        this.newMatchList = newMatchList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_match_story, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String hisUid = newMatchList.get(position).getUid();
        String profileImage = newMatchList.get(position).getProfileImage();
        String hisName = newMatchList.get(position).getUserName();

        holder.hisName.setText(hisName);
        try {
            Log.d("profileImage", profileImage);
            Picasso.get().load(profileImage).placeholder(R.drawable.ic_account_black).into(holder.profileIv);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.ic_account_black).into(holder.profileIv);
        }
    }

    @Override
    public int getItemCount() {
        return newMatchList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView profileIv;
        TextView hisName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profileIv = itemView.findViewById(R.id.profileIv);
            hisName = itemView.findViewById(R.id.userName);
        }
    }
}
