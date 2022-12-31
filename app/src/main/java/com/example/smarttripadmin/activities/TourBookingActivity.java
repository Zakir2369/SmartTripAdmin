package com.example.smarttripadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;
import com.example.smarttripadmin.databinding.ActivityTourBookingBinding;

public class TourBookingActivity extends AppCompatActivity {
    private ActivityTourBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTourBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}