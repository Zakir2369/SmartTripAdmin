package com.example.smarttripadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.smarttripadmin.databinding.ActivityLoginBinding;
import com.example.smarttripadmin.util.Constants;
import com.example.smarttripadmin.util.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private PreferenceManager preferenceManager;
    private String userName, phone, email, password;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
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

        binding.signinHaventAccount.setOnClickListener(view -> {
            Intent welcome3 = new Intent(this, MainActivity.class);
            startActivity(welcome3);
            finish();
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
                            String userName = document.getString(Constants.KEY_USER_NAME);
                            String email = document.getString(Constants.KEY_EMAIL);
                            String phone = document.getString(Constants.KEY_PHONE);

                            makeToast("Successfully Logged In");
                            preferenceManager.putSting(Constants.KEY_EMAIL, email);
                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED, true);
                            preferenceManager.putSting(Constants.KEY_USER_NAME, userName);
                            preferenceManager.putSting(Constants.KEY_PHONE, phone);
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();

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