package com.example.smarttripadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.smarttripadmin.activities.AddBusActivity;
import com.example.smarttripadmin.activities.AddHotelActivity;
import com.example.smarttripadmin.activities.BusBookingActivity;
import com.example.smarttripadmin.activities.BusListActivity;
import com.example.smarttripadmin.activities.CreateTourActivity;
import com.example.smarttripadmin.activities.HotelBookingActivity;
import com.example.smarttripadmin.activities.HotelListActivity;
import com.example.smarttripadmin.activities.ManageUserActivity;
import com.example.smarttripadmin.activities.TourBookingActivity;
import com.example.smarttripadmin.activities.TourListActivity;
import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends AppCompatActivity {
    private ActivityAdminMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();


    }

    private void setListeners() {


        binding.hotel.setOnClickListener(v->{
            if(binding.constraintLayout1.getVisibility() == View.GONE) {
                binding.constraintLayout1.setVisibility(View.VISIBLE);
                binding.hotelPanel.setImageResource(R.drawable.ic_expand_less);
            } else {
                binding.constraintLayout1.setVisibility(View.GONE);
                binding.hotelPanel.setImageResource(R.drawable.ic_expand_more);
            }
        });

        binding.bus.setOnClickListener(v->{
            if(binding.constraintLayout2.getVisibility() == View.GONE) {
                binding.constraintLayout2.setVisibility(View.VISIBLE);
                binding.busPanel.setImageResource(R.drawable.ic_expand_less);
            } else {
                binding.constraintLayout2.setVisibility(View.GONE);
                binding.busPanel.setImageResource(R.drawable.ic_expand_more);
            }
        });

        binding.tour.setOnClickListener(v->{
            if(binding.constraintLayout3.getVisibility() == View.GONE) {
                binding.constraintLayout3.setVisibility(View.VISIBLE);
                binding.tourPanel.setImageResource(R.drawable.ic_expand_less);
            } else {
                binding.constraintLayout3.setVisibility(View.GONE);
                binding.tourPanel.setImageResource(R.drawable.ic_expand_more);
            }
        });

        binding.profileBackBtn.setOnClickListener(view->onBackPressed());
        binding.allUser.setOnClickListener(view->{
            Intent intent = new Intent(AdminMainActivity.this, ManageUserActivity.class);
            startActivity(intent);
        });
        binding.registerHotel.setOnClickListener(view->{
            Intent intent = new Intent(AdminMainActivity.this, AddHotelActivity.class);
            startActivity(intent);
        });
        binding.registerBus.setOnClickListener(view->{
            Intent intent = new Intent(AdminMainActivity.this, AddBusActivity.class);
            startActivity(intent);
        });
        binding.createTour.setOnClickListener(view->{
            Intent intent = new Intent(AdminMainActivity.this, CreateTourActivity.class);
            startActivity(intent);
        });
        binding.hotelList.setOnClickListener(view->{
            Intent intent = new Intent(AdminMainActivity.this, HotelListActivity.class);
            startActivity(intent);
        });
        binding.busList.setOnClickListener(view->{
            Intent intent = new Intent(AdminMainActivity.this, BusListActivity.class);
            startActivity(intent);
        });
        binding.tourList.setOnClickListener(view->{
            Intent intent = new Intent(AdminMainActivity.this, TourListActivity.class);
            startActivity(intent);
        });
        binding.busBooking.setOnClickListener(view->{
            Intent intent = new Intent(AdminMainActivity.this, BusBookingActivity.class);
            startActivity(intent);
        });
        binding.hotelBooking.setOnClickListener(view->{
            Intent intent = new Intent(AdminMainActivity.this, HotelBookingActivity.class);
            startActivity(intent);
        });
        binding.tourBooking.setOnClickListener(view->{
            Intent intent = new Intent(AdminMainActivity.this, TourBookingActivity.class);
            startActivity(intent);
        });

    }
}