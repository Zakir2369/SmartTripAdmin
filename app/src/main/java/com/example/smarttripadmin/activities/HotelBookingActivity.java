package com.example.smarttripadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;
import com.example.smarttripadmin.databinding.ActivityHotelBookingBinding;

public class HotelBookingActivity extends AppCompatActivity {
    private ActivityHotelBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}