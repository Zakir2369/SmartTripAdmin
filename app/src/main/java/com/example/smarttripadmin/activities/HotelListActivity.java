package com.example.smarttripadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.smarttripadmin.adapters.HotelAdapter;
import com.example.smarttripadmin.databinding.ActivityHotelListBinding;
import com.example.smarttripadmin.listeners.HotelListener;
import com.example.smarttripadmin.models.Hotel;
import com.example.smarttripadmin.util.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HotelListActivity extends AppCompatActivity implements HotelListener {
    private ActivityHotelListBinding binding;
    private ArrayList<Hotel> hotelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getHotelDataFromFirebase();
    }

    public void getHotelDataFromFirebase() {
        isLoading(true);
        hotelList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_HOTEL_DB)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Hotel hotel = new Hotel();
                            hotel.hotelId = document.getId();
                            hotel.hotelName = document.getString(Constants.KEY_HOTEL_NAME);
                            hotel.hotelLocation = document.getString(Constants.KEY_HOTEL_LOCATION);
                            hotel.hotelDetails = document.getString(Constants.KEY_HOTEL_DETAILS);
                            hotel.hotelPrice = document.getString(Constants.KEY_HOTEL_PRICE);
                            hotel.hotelEmail = document.getString(Constants.KEY_HOTEL_EMAIL);
                            hotel.hotelPhone = document.getString(Constants.KEY_HOTEL_PHONE);
                            hotel.roomCount = document.getString(Constants.KEY_HOTEL_ROOM_COUNT);
                            hotel.wifi = document.getBoolean(Constants.KEY_HOTEL_WIFI);
                            hotel.ac = document.getBoolean(Constants.KEY_HOTEL_AC);
                            hotel.restaurant = document.getBoolean(Constants.KEY_HOTEL_DINING);
                            hotel.parking = document.getBoolean(Constants.KEY_HOTEL_PARKING);
                            hotel.hotelImage = document.getString(Constants.KEY_HOTEL_IMAGE);
                            hotel.hotelReview = document.getString(Constants.KEY_HOTEL_REVIEW);
                            hotelList.add(hotel);
                        }
                        if(hotelList.size()>0) {
                            setAllHotel();
                        }

                        isLoading(false);
                    }
                });

    }

    private void setAllHotel() {
        HotelAdapter hotelAdapter = new HotelAdapter(hotelList, this);
        binding.recyclerView.setAdapter(hotelAdapter);
    }

    private void isLoading(Boolean loading) {
        if(loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUserClicked(Hotel hotel) {
        Intent intent = new Intent(this, HotelDetailsActivity.class);
        intent.putExtra(Constants.KEY_HOTEL_NAME, hotel);
        startActivity(intent);
    }
}