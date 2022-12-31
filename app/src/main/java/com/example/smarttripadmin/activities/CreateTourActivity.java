package com.example.smarttripadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;
import com.example.smarttripadmin.databinding.ActivityCreateTourBinding;

public class CreateTourActivity extends AppCompatActivity {
    private ActivityCreateTourBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTourBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}