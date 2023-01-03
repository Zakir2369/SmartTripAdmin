package com.example.smarttripadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.smarttripadmin.databinding.ActivityHotelDetailsBinding;
import com.example.smarttripadmin.models.Hotel;
import com.example.smarttripadmin.util.Constants;
import com.example.smarttripadmin.util.PreferenceManager;

public class HotelDetailsActivity extends AppCompatActivity {
    private ActivityHotelDetailsBinding binding;
    private boolean clicked1=false;
    private boolean clicked2=false;
    private Hotel hotel;
    private int roomCount = 0;
    private PreferenceManager preferenceManager;
    private String checkInDate, checkOutDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        hotel = (Hotel) getIntent().getSerializableExtra(Constants.KEY_HOTEL_NAME);
        binding.hotelName.setText(hotel.hotelName);
        binding.hotelDetails.setText(hotel.hotelDetails);
        binding.hotelImage.setImageBitmap(getBitmapFromEncodedString(hotel.hotelImage));
        binding.location.setText(hotel.hotelLocation);
        binding.hotelPrice.setText("BDT "+hotel.hotelPrice);
        binding.roomCount.setText(hotel.roomCount);

        if(hotel.wifi) {
            binding.hotelWifi.setVisibility(View.VISIBLE);
        }
        if(hotel.ac) {
            binding.hotelAc.setVisibility(View.VISIBLE);
        }
        if(hotel.restaurant) {
            binding.hotelDinning.setVisibility(View.VISIBLE);
        }
        if (hotel.parking) {
            binding.hotelParking.setVisibility(View.VISIBLE);
        }

        binding.hotelReadMore.setOnClickListener(v -> {
            if(binding.hotelDetails.getMaxLines() ==2) {
                binding.hotelDetails.setMaxLines(10);

            } else {
                binding.hotelDetails.setMaxLines(4);
            }
        });

    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void isLoading(Boolean loading) {
        if(loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}