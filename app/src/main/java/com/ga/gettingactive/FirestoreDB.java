package com.ga.gettingactive;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreDB {

    private FirestoreDB(){}

    static public FirebaseFirestore db = FirebaseFirestore.getInstance();
}
