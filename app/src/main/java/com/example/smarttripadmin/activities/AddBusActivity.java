package com.example.smarttripadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smarttripadmin.databinding.ActivityAddBusBinding;
import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;
import com.example.smarttripadmin.databinding.ActivityCreateTourBinding;

public class AddBusActivity extends AppCompatActivity {
    private ActivityAddBusBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}