package com.example.sdaassign32020;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WelcomeTab extends Fragment {
    /**
     * The default method is used to create a new instance of
     * this fragment.
     * @return A new instance of fragment WelcomeTab.
     */
    public WelcomeTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.welcometab, container, false);
    }
}