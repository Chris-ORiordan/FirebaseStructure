package com.example.standard.firebasestructure;

import com.google.firebase.storage.*;

public final class Constants {

    public static final StorageReference STORAGE_REF = FirebaseStorage.getInstance().getReference();

    public static final int REQUEST_GALLERY = 2;
}