package com.example.smarttripadmin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.smarttripadmin.databinding.ActivityHotelDetailsBinding;
import com.example.smarttripadmin.models.Hotel;
import com.example.smarttripadmin.util.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HotelDetailsActivity extends AppCompatActivity {
    private ActivityHotelDetailsBinding binding;
    private Hotel hotel;
    private ArrayList<Uri> uriList;
    private ArrayList<String> pdfUrlList;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHotelDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setCancelable(false);
        storageReference = FirebaseStorage.getInstance().getReference();
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
            hotel.hotelPrice = Double.parseDouble(binding.hotelPrice.getText().toString().trim());
            hotel.roomCount = Integer.parseInt(binding.hotelTotalRoom.getText().toString().trim());
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
                    uriList = new ArrayList<>();
                    assert result.getData() != null;
                    if (result.getData().getClipData() != null) {
                        ClipData mClipData = result.getData().getClipData();
                        int count = result.getData().getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri uri = result.getData().getClipData().getItemAt(i).getUri();
                            uriList.add(uri);
                        }

                    } else {
                        Uri uri = result.getData().getData();
                        uriList.add(uri);
                    }

                }
                progressDialog.show();
                uploadImage(uriList);
            }
    );




    @SuppressLint("SetTextI18n")
    private void uploadImage(ArrayList<Uri> uri) {
        pdfUrlList = new ArrayList<>();

        for(int i=0; i<uri.size(); i++) {
            final StorageReference filepath = storageReference
                    .child(
                            getFileName(uri.get(i))+" "+new Date()
                    );
            UploadTask uploadTask = filepath.putFile(uri.get(i));

            int finalI = i+1;
            uploadTask.addOnProgressListener(snapshot -> {
                progressDialog.setTitle("Uploading Image "+ finalI);
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded "+ (int)progress + "%");
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    String pdfUrl = filepath.getName();
                    pdfUrlList.add(pdfUrl);
                } else {
                    makeToast("Failed!");
                }

                if (finalI==uri.size()){
                    progressDialog.dismiss();
                }
            });
        }

    }

    private String getFileName(Uri uri) {
        DocumentFile file = DocumentFile.fromSingleUri(this, uri);
        assert file != null;
        return file.getName();
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

        for(int i=0; i<pdfUrlList.size(); i++) {
            hotels.put(Constants.KEY_HOTEL_IMAGE+i, pdfUrlList.get(i));
        }


        db.collection(Constants.KEY_HOTEL_DB)
                .add(hotels)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        makeToast("Completed!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        makeToast("Failed!");
                    }
                });
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
            }else if(uriList.size()<1) {
                makeToast("Select Image");
                return;
            }
            createHotelUser();
        } catch (Exception e) {
            Log.d("Afridi", e.getMessage());
        }

    }

    private void createHotelUser() {
        mAuth.createUserWithEmailAndPassword(hotel.hotelEmail, hotel.hotelPassword)
                .addOnSuccessListener(authResult -> makeToast("User created."))
                .addOnFailureListener(e -> makeToast(e.getMessage()));
        addToDatabase();
    }



}