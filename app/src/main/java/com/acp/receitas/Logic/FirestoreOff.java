package com.acp.receitas.Logic;

import android.app.Application;

import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class FirestoreOff extends Application {

    private FirebaseFirestore fireDB;
    private FirebaseFirestoreSettings fbs;

    @Override
    public void onCreate() {
        super.onCreate();
        fireDB = FirebaseFirestore.getInstance();
        fbs = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        fireDB.setFirestoreSettings(fbs);

    }
}
