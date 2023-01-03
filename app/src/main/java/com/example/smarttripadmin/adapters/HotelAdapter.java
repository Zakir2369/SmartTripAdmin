package com.example.smarttripadmin.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttripadmin.databinding.ItemContainerHotelBinding;
import com.example.smarttripadmin.listeners.HotelListener;
import com.example.smarttripadmin.models.Hotel;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.AllHotelViewHolder >{

    private final List<Hotel> hotelList;
    private final HotelListener hotelListener;

    public HotelAdapter(List<Hotel> hotelList, HotelListener hotelListener) {
        this.hotelList = hotelList;
        this.hotelListener = hotelListener;
    }

    @NonNull
    @Override
    public AllHotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerHotelBinding itemContainerHotelBinding = ItemContainerHotelBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AllHotelViewHolder(itemContainerHotelBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllHotelViewHolder holder, int position) {
        holder.setHotelData(hotelList.get(position));
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    class AllHotelViewHolder extends RecyclerView.ViewHolder{
        ItemContainerHotelBinding binding;
        public AllHotelViewHolder(ItemContainerHotelBinding itemContainerHotelBinding) {
            super(itemContainerHotelBinding.getRoot());
            binding = itemContainerHotelBinding;
        }
        @SuppressLint("SetTextI18n")
        void setHotelData(Hotel hotel){
            binding.hotelImg.setImageBitmap(getBitmapFromEncodedString(hotel.hotelImage));
            binding.hotelName.setText(hotel.hotelName);
            binding.location.setText(hotel.hotelLocation);
            binding.price.setText("BDT "+hotel.hotelPrice);
            binding.getRoot().setOnClickListener(view -> hotelListener.onUserClicked(hotel));
        }
        private Bitmap getBitmapFromEncodedString(String encodedImage) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

    }
}
