package com.example.smarttripadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;
import com.example.smarttripadmin.databinding.ActivityHotelBookingBinding;
import com.example.smarttripadmin.models.Hotel;
import com.example.smarttripadmin.util.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HotelBookingActivity extends AppCompatActivity {
    private ActivityHotelBookingBinding binding;
    private ArrayList<Hotel> hotelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }


}