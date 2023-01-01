package com.example.smarttripadmin.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.smarttripadmin.databinding.ActivityAddHotelBinding;
import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;
import com.example.smarttripadmin.databinding.ActivityCreateTourBinding;
import com.example.smarttripadmin.models.Hotel;
import com.example.smarttripadmin.util.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddHotelActivity extends AppCompatActivity {
    private ActivityAddHotelBinding binding;
    private Hotel hotel;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddHotelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setCancelable(false);
        setListeners();
    }


    private void getHotelInfo() {
        try{
            hotel = new Hotel();
            hotel.hotelEmail = binding.hotelEmailAddress.getText().toString().trim();
            hotel.hotelPassword = binding.hotelPassword.getText().toString().trim();
            hotel.hotelName = binding.hotelName.getText().toString().trim();
            hotel.hotelDetails = binding.hotelDetails.getText().toString().trim();
            hotel.hotelPhone = binding.hotelPhone.getText().toString().trim();
            hotel.hotelLocation = binding.hotelLocation.getText().toString().trim();
            hotel.hotelPrice = binding.hotelPrice.getText().toString().trim();
            hotel.roomCount = binding.hotelTotalRoom.getText().toString().trim();
            hotel.wifi = binding.wifi.isChecked();
            hotel.ac = binding.ac.isChecked();
            hotel.restaurant = binding.restorant.isChecked();
            hotel.parking = binding.parking.isChecked();
            isNotEmpty();
        } catch (Exception e) {
            makeToast(e.getMessage());
        }

    }

    private void setListeners() {
        binding.hotelImage.setOnClickListener(view -> {
            if(checkPermissions()) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                pickImage.launch(intent);

            } else {
                makeToast("Storage permission denied");
                askPermission();
            }

        });
        binding.saveInfo.setOnClickListener(view->getHotelInfo());

    }

    @SuppressLint("SetTextI18n")
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == RESULT_OK) {
                    assert result.getData() != null;
                    if (result.getData().getClipData() != null) {
                        int count = result.getData().getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {

                            Uri uri = result.getData().getClipData().getItemAt(i).getUri();
                            try{
                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                image = encodeImage(bitmap);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        Uri uri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            image = encodeImage(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
    );

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 512;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap , previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private Boolean checkPermissions(){
        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void makeToast(String value) {
        Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
    }


    private void askPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void addToDatabase() {
        Map<String, Object> hotels = new HashMap<>();
        hotels.put(Constants.KEY_HOTEL_NAME, hotel.hotelName);
        hotels.put(Constants.KEY_HOTEL_EMAIL, hotel.hotelEmail);
        hotels.put(Constants.KEY_HOTEL_PHONE, hotel.hotelPhone);
        hotels.put(Constants.KEY_HOTEL_PASSWORD, hotel.hotelPassword);
        hotels.put(Constants.KEY_HOTEL_DETAILS, hotel.hotelDetails);
        hotels.put(Constants.KEY_HOTEL_LOCATION, hotel.hotelLocation);
        hotels.put(Constants.KEY_HOTEL_PRICE, hotel.hotelPrice);
        hotels.put(Constants.KEY_HOTEL_ROOM_COUNT, hotel.roomCount);
        hotels.put(Constants.KEY_HOTEL_WIFI, hotel.wifi);
        hotels.put(Constants.KEY_HOTEL_AC, hotel.ac);
        hotels.put(Constants.KEY_HOTEL_DINING, hotel.restaurant);
        hotels.put(Constants.KEY_HOTEL_PARKING, hotel.parking);

        hotels.put(Constants.KEY_HOTEL_IMAGE, image);


        db.collection(Constants.KEY_HOTEL_DB)
                .add(hotels)
                .addOnSuccessListener(documentReference -> makeToast("Successfully added to database."))
                .addOnFailureListener(e -> makeToast(e.getMessage()));
    }


    private void isNotEmpty() {

        try{
            if(hotel.hotelName.isEmpty()) {
                binding.hotelName.setError("This field is required!");
                binding.hotelName.requestFocus();
                return;
            } else if(hotel.hotelPhone.isEmpty()) {
                binding.hotelPhone.setError("This field is required!");
                return;
            } else if(hotel.hotelPhone.length()<11) {
                binding.hotelPhone.setError("Enter valid phone number.");
                return;
            } else if(hotel.hotelEmail.isEmpty()){
                binding.hotelEmailAddress.setError("This field is required!");
                return;
            } else if(!hotel.hotelEmail.matches("[a-zA-Z0-9._-]+@gmail.com+")) {
                binding.hotelEmailAddress.setError("Invalid email.");
                return;
            } else if(hotel.hotelPassword.isEmpty()) {
                binding.hotelPassword.setError("This field is required!");
                return;
            }else if(hotel.hotelPassword.length()<6 ) {
                binding.hotelPassword.setError("password at least 6 characters");
                return;
            } else if(hotel.hotelDetails.isEmpty()) {
                binding.hotelDetails.setError("This field is required!");
                return;
            }else if(hotel.hotelLocation.isEmpty()) {
                binding.hotelLocation.setError("This field is required!");
                return;
            }else if(String.valueOf( hotel.roomCount).isEmpty()) {
                binding.hotelTotalRoom.setError("Enter room number!");
                return;
            }else if(image.isEmpty()) {
                makeToast("Select Image");
                return;
            }
            createHotelUser();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createHotelUser() {
        mAuth.createUserWithEmailAndPassword(hotel.hotelEmail, hotel.hotelPassword)
                .addOnSuccessListener(authResult -> {
                    makeToast("User created.");
                    addToDatabase();
                })
                .addOnFailureListener(e -> makeToast(e.getMessage()));

    }
}