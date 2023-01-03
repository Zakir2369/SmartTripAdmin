package com.example.smarttripadmin.models;

import java.io.Serializable;

public class User implements Serializable {
    public String userId, userName, phoneNumber, email, profilePicture, adminType;
    public Boolean isAdmin;
}
