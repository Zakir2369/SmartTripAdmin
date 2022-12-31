package com.example.smarttripadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;
import com.example.smarttripadmin.databinding.ActivityBusBookingBinding;
import com.example.smarttripadmin.databinding.ActivityCreateTourBinding;

public class BusBookingActivity extends AppCompatActivity {
    private ActivityBusBookingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}