package com.example.smarttripadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.smarttripadmin.admin.BusAdminActivity;
import com.example.smarttripadmin.admin.HotelAdminActivity;
import com.example.smarttripadmin.databinding.ActivityAdminSigninBinding;
import com.example.smarttripadmin.databinding.ActivityLoginBinding;
import com.example.smarttripadmin.util.Constants;
import com.example.smarttripadmin.util.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class AdminSigninActivity extends AppCompatActivity {

    private ActivityAdminSigninBinding binding;
    private PreferenceManager preferenceManager;
    private String email, password, adminType, phone, userName;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminSigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setListeners();
    }

    private void setListeners() {
        binding.signinButton.setOnClickListener(view -> {
            isLoading(true);
            getUserData();
        });
    }

    private void getUserData() {
        email = binding.signinEmailEditText.getText().toString().trim();
        password = binding.signinPasswordEditText.getText().toString().trim();

        if (isNotEmpty()) {
            signIn();
        }
    }

    private Boolean isNotEmpty() {

        if (email.isEmpty()) {
            binding.signinEmailEditText.setError("This field is required!");
            return false;
        } else if (!email.matches("[a-zA-Z0-9._-]+@gmail.com+")) {
            binding.signinEmailEditText.setError("Invalid email.");
            return false;
        } else if (password.isEmpty()) {
            binding.signinPasswordEditText.setError("This field is required!");
            return false;
        } else if (password.length() < 6) {
            binding.signinPasswordEditText.setError("password at least 6 characters");
            return false;
        }
        return true;
    }

    private void signIn() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user!=null) {
                            getDataFromFirebase(user.getUid());
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    isLoading(false);
                });
    }

    private void getDataFromFirebase(String userId) {
        db.collection("users")
                .whereEqualTo(Constants.KEY_USER_ID, userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userName = document.getString(Constants.KEY_USER_NAME);
                            email = document.getString(Constants.KEY_EMAIL);
                            phone = document.getString(Constants.KEY_PHONE);
                            isAdmin = document.getBoolean(Constants.KEY_IS_ADMIN);


                            if(isAdmin!=null && isAdmin) {
                                adminType = document.getString(Constants.KEY_ADMIN_TYPE);
                                if(adminType!=null)  {
                                    makeToast("Successfully Logged In");
                                    preferenceManager.putSting(Constants.KEY_EMAIL, email);
                                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED, true);
                                    preferenceManager.putSting(Constants.KEY_USER_NAME, userName);
                                    preferenceManager.putSting(Constants.KEY_PHONE, phone);

                                    if(adminType.equals("super")) {
                                        Intent intent = new Intent(this, AdminMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else if(adminType.equals("bus")) {
                                        Intent intent = new Intent(this, BusAdminActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else if(adminType.equals("hotel")) {
                                        Intent intent = new Intent(this, HotelAdminActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                makeToast("Something went to wrong.");
                                mAuth.signOut();
                            } else {
                                makeToast("Only for admin login");
                                mAuth.signOut();
                            }

                        }
                    } else {
                        makeToast("Login Failed!");
                    }
                });
    }

    private void makeToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void isLoading(Boolean loading) {
        if (loading) {
            binding.progress2.setVisibility(View.VISIBLE);
        } else {
            binding.progress2.setVisibility(View.GONE);
        }
    }
}