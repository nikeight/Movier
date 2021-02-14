package com.appchef.movier.ProfileFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.appchef.movier.R;
import com.appchef.movier.navBarActivities.ProfileActivity;

public class BaseProfileFragment extends Fragment {

    private ImageView addMediaIv, settingIv, editProfileIv;
    private Fragment selectedFragment;

    public BaseProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_profile, container, false);

        Init(view);
        OnClickFragmentChangeEvents();
        return view;
    }

    private void OnClickFragmentChangeEvents() {
        addMediaIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = new AddImagesFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.profileFrameContainer,selectedFragment)
                        .commit();
            }
        });

        settingIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = new SettingFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.profileFrameContainer,selectedFragment)
                        .commit();
            }
        });

        editProfileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFragment = new EditProfileFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.profileFrameContainer,selectedFragment)
                        .commit();
            }
        });
    }

    private void Init(View view) {
        addMediaIv = view.findViewById(R.id.addMediaIv);
        settingIv = view.findViewById(R.id.settingProfileIv);
        editProfileIv = view.findViewById(R.id.editProfileIv);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}