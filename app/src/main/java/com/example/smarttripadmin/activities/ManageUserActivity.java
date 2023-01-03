package com.example.smarttripadmin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.smarttripadmin.adapters.UserAdapter;
import com.example.smarttripadmin.databinding.ActivityAdminMainBinding;
import com.example.smarttripadmin.databinding.ActivityManageUserBinding;
import com.example.smarttripadmin.listeners.UserListeners;
import com.example.smarttripadmin.models.User;
import com.example.smarttripadmin.util.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ManageUserActivity extends AppCompatActivity implements UserListeners {
    private ActivityManageUserBinding binding;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getUserInfo();
        setListeners();
    }

    private void setListeners() {
        binding.backBtn.setOnClickListener(v->{
            onBackPressed();
        });

    }

    private void getUserInfo() {
        isLoading(true);
        users = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_USER_DB)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = new User();
                            user.userId = document.getId();
                            user.userName = document.getString(Constants.KEY_USER_NAME);
                            user.phoneNumber = document.getString(Constants.KEY_PHONE);
                            user.email = document.getString(Constants.KEY_EMAIL);
                            user.profilePicture = document.getString(Constants.KEY_USER_IMAGE);
                            user.isAdmin = document.getBoolean(Constants.KEY_IS_ADMIN);
                            user.adminType = document.getString(Constants.KEY_ADMIN_TYPE);
                            users.add(user);
                        }
                        Log.d("Afridi", users.size()+"");
                        if(users.size()>0) {
                            setUserAdapter();
                        }

                        isLoading(false);
                    }
                });
    }

    private void setUserAdapter() {
        UserAdapter usersAdapter = new UserAdapter(users, this);
        binding.recyclerView.setAdapter(usersAdapter);
    }

    private void isLoading(Boolean loading) {
        if(loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUserClicked(User user) {
//        Intent intent = new Intent(this, HotelDetailsActivity.class);
//        intent.putExtra(Constants.KEY_HOTEL_NAME, hotel);
//        startActivity(intent);
    }
}