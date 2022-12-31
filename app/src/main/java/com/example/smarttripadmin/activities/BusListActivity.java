package com.example.smarttripadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;
import com.example.smarttripadmin.databinding.ActivityBusListBinding;
import com.example.smarttripadmin.databinding.ActivityCreateTourBinding;

public class BusListActivity extends AppCompatActivity {
    private ActivityBusListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}