package com.example.premssion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainFragment extends Fragment {

private TextView fragment_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        fragment_txt = view.findViewById(R.id.fragment_txt);
        String sTitle = getArguments().getString("title");
        fragment_txt.setText(sTitle);
        return view;
    }
}