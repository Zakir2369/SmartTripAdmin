package com.example.smarttripadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;
import com.example.smarttripadmin.databinding.ActivityHotelListBinding;

public class HotelListActivity extends AppCompatActivity {
    private ActivityHotelListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}