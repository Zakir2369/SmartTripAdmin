package com.example.smarttripadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;
import com.example.smarttripadmin.databinding.ActivityTourListBinding;

public class TourListActivity extends AppCompatActivity {
    private ActivityTourListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTourListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}