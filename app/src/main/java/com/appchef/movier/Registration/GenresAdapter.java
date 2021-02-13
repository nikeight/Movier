package com.appchef.movier.Registration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appchef.movier.R;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.ViewHolder>{

    String TAG = "TestingListItem";
    private List<String> categoryList = new ArrayList<>();

    public GenresAdapter(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genres_bubble,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.categoryText.setText(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView categoryText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryText = itemView.findViewById(R.id.genresCategoryTv);
        }
    }
}
