package com.appchef.movier.ProfileFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appchef.movier.R;

public class AddImagesFragment extends Fragment {

    public AddImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_images, container, false);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == keyEvent.KEYCODE_BACK){
                   getFragmentManager().beginTransaction().replace(R.id.profileFrameContainer,new BaseProfileFragment())
                           .commit();
                    return true;
                }

                return false;
            }
        });
    }
}