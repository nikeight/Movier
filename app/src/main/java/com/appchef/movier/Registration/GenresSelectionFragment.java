package com.appchef.movier.Registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.GeneratedAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.appchef.movier.R;
import com.appchef.movier.navBarActivities.HomeActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenresSelectionFragment extends Fragment {

    // Views
    private Button proceedToHomeBtn;
    private RecyclerView genresRecyclerView;
    private List<String> category;

    public GenresSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_genres_selection, container, false);

        Init(view);
        AddItemToList();
        AdapterSetUp();

        // Onclick Events.
        proceedToHomeBtn.setOnClickListener(view1 -> getToHomeScreen());

        return view;
    }

    private void getToHomeScreen() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finishAffinity();
    }

    private void AddItemToList() {
      category = Arrays.asList("Horror","Comedy","Action","Thriller","Suspense","Mystery","Documentation","Biography");
    }

    private void AdapterSetUp() {
        genresRecyclerView.setHasFixedSize(true);
        genresRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        GenresAdapter genresAdapter = new GenresAdapter(category);
        genresRecyclerView.setAdapter(genresAdapter);
    }

    private void Init(View v) {
        proceedToHomeBtn = v.findViewById(R.id.proceedToHomeBtn);
        genresRecyclerView = v.findViewById(R.id.genresRecyclerView);
    }
}