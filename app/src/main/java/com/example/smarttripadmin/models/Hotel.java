package com.example.smarttripadmin.models;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;
import java.util.Date;

public class Hotel implements Serializable {
    public String hotelName, hotelLocation, hotelReview, hotelDetails;
    public String hotelPhone, hotelEmail;
    public String checkIn, checkOut;
    public String roomCount, hotelId, hotelPassword;
    public String hotelPrice;
    public String hotelImage, hotelImage1, hotelImage2, hotelImage3;
    public Boolean wifi=false, ac=false, restaurant=false, parking=false;


    public Hotel() {

    }




}
