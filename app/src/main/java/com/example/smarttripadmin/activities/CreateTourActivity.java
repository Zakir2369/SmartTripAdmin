package com.example.smarttripadmin.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.smarttripadmin.MainActivity;
import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;
import com.example.smarttripadmin.databinding.ActivityCreateTourBinding;
import com.example.smarttripadmin.util.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateTourActivity extends AppCompatActivity {
    private ActivityCreateTourBinding binding;
    private String packageName, location, price, durations, overview;
    private String mainImage, imageOne, imageTwo, imageThree, tourDate;
    private String dayOne, dayTwo, dayThree;
    private ArrayList<String> imagesEncodedList;
    private ArrayList<Uri> uriList;
    private ArrayList<Bitmap> bitmaps;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTourBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        uriList = new ArrayList<>();
        imagesEncodedList = new ArrayList<>();
        bitmaps = new ArrayList<>();
        binding.tourDate.setText(getReadableDateTime(new Date()));
    }

    private void setListeners() {
        binding.mainImage.setOnClickListener(view->{
            if(checkPermissions()) {
                actionImage();
            } else {
                makeToast("Storage permission denied");
                askPermission();
            }

        });
        binding.createTour.setOnClickListener(view -> {
            getInputData();
        });

        binding.tourDate.setOnClickListener(view -> {
            datePicker();
        });

        binding.profileBackBtn.setOnClickListener(view -> onBackPressed());
    }

    private void actionImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        pickImage.launch(intent);
    }

    @SuppressLint("SetTextI18n")
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == RESULT_OK && result.getData() != null) {

                    if (result.getData().getClipData() != null) {
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

                try {
                    getUriFromList();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
    );

    private void getUriFromList() throws FileNotFoundException {
        for(Uri uri : uriList) {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imagesEncodedList.add(encodeImage(bitmap));
            bitmaps.add(bitmap);
        }
        setImageView();
    }

    private void setImageView() {
        binding.textGallery.setVisibility(View.VISIBLE);
        binding.imageGallery.setVisibility(View.VISIBLE);
        if(bitmaps.size() ==4) {
            binding.imageOne.setImageBitmap(bitmaps.get(0));
            binding.imageTwo.setImageBitmap(bitmaps.get(1));
            binding.imageThree.setImageBitmap(bitmaps.get(2));
            binding.imageFour.setImageBitmap(bitmaps.get(3));
        }

    }

    private void getInputData() {
        binding.createTour.setClickable(false);
        packageName = binding.tourName.getText().toString().trim();
        location = binding.tourLocation.getText().toString().trim();
        price = binding.tourPrice.getText().toString().trim();
        durations = binding.tourDuration.getText().toString().trim();
        overview = binding.tourOverview.getText().toString().trim();
        dayOne = binding.tourDayOne.getText().toString().trim();
        dayTwo = binding.tourDayTwo.getText().toString().trim();
        dayThree = binding.tourDayThree.getText().toString().trim();
        tourDate = binding.tourDate.getText().toString().trim();

        if(imagesEncodedList.size() == 4) {
            mainImage = imagesEncodedList.get(0);
            imageOne = imagesEncodedList.get(1);
            imageTwo = imagesEncodedList.get(2);
            imageThree = imagesEncodedList.get(3);
        } else {
            makeToast("Select 4 images.");
        }

        checkDataValidation();
    }

    private void checkDataValidation() {
        if(packageName.isEmpty()) {
            binding.tourName.setError("Enter a package name.");
            binding.tourName.requestFocus();
            return;
        }
        if(location.isEmpty()) {
            binding.tourLocation.setError("Enter tour location.");
            binding.tourLocation.requestFocus();
            return;
        }
        if(price.isEmpty()) {
            binding.tourPrice.setError("Enter package price.");
            binding.tourPrice.requestFocus();
            return;
        }
        if(durations.isEmpty()) {
            binding.tourDuration.setError("Enter a tour duration.");
            binding.tourDuration.requestFocus();
            return;
        }
        if(overview.isEmpty()) {
            binding.tourOverview.setError("Enter a tour overview.");
            binding.tourOverview.requestFocus();
            return;
        }
        if(tourDate.isEmpty()) {
            binding.tourDate.setError("Enter a tour date.");
            binding.tourDate.requestFocus();
            return;
        }
        if(dayOne.isEmpty()) {
            binding.tourDayOne.setError("Enter first day plan.");
            binding.tourDayOne.requestFocus();
            return;
        }
        if(dayTwo.isEmpty()) {
            binding.tourDayTwo.setError("Enter second day plan.");
            binding.tourDayTwo.requestFocus();
            return;
        }
        if(dayThree.isEmpty()) {
            binding.tourDayThree.setError("Enter second day plan.");
            binding.tourDayThree.requestFocus();
            return;
        }
        if(imagesEncodedList.size() != 4) {
            makeToast("Select 4 images");
            return;
        }
        addToDatabase();
    }

    private void addToDatabase() {
        isLoading(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> packageInfo = new HashMap<>();
        packageInfo.put(Constants.KEY_TOUR_NAME, packageName);
        packageInfo.put(Constants.KEY_TOUR_LOCATION, location);
        packageInfo.put(Constants.KEY_TOUR_PRICE, price);
        packageInfo.put(Constants.KEY_TOUR_DURATION, durations);
        packageInfo.put(Constants.KEY_TOUR_DETAILS, overview);
        packageInfo.put(Constants.KEY_TOUR_DATE, tourDate);
        packageInfo.put(Constants.KEY_TOUR_DAY_ONE, dayOne);
        packageInfo.put(Constants.KEY_TOUR_DAY_TWO, dayTwo);
        packageInfo.put(Constants.KEY_TOUR_DAY_THREE, dayThree);
        packageInfo.put(Constants.KEY_TOUR_MAIN_IMAGE, mainImage);
        packageInfo.put(Constants.KEY_TOUR_GALLERY_ONE, imageOne);
        packageInfo.put(Constants.KEY_TOUR_GALLERY_TWO, imageTwo);
        packageInfo.put(Constants.KEY_TOUR_GALLERY_THREE, imageThree);
        
        db.collection(Constants.KEY_TOUR_DB)
                .add(packageInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        makeToast("Successfully Added to database.");
                        isLoading(false);
                        Intent intent = new Intent(CreateTourActivity.this,CreateTourActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        makeToast("Failed to add database "+e.getMessage());
                        isLoading(false);
                    }
                });
    }

    private void datePicker() {

        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SetTextI18n")
        DatePickerDialog datePickerDialog = new DatePickerDialog(

                CreateTourActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalDate localDate = LocalDate.of(year1, monthOfYear+1, dayOfMonth);
                        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                        binding.tourDate.setText(getReadableDateTime(date));
                    }

                },

                year, month, day);
        datePickerDialog.show();
    }

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(date);
    }



    private String encodeImage(Bitmap bitmap){
        int previewWidth = 540;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap , previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void makeToast(String value) {
        Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private Boolean checkPermissions(){
        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void isLoading(Boolean loading) {
        if(loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
    
}