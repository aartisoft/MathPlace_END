package com.math4.user.mathplace;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.math4.user.mathplace.R;

public class About_us extends Fragment {
    SharedPreferences sPref;
    TextView message,text_activity;
    // OnDataPass dataPasser;

    @Override
    // Переопределяем метод onCreateView
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);
        return view;
    }
}