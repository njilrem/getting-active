package com.ga.gettingactive.pref;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.ga.gettingactive.FirestoreDB;
import com.ga.gettingactive.MainActivity;
import com.ga.gettingactive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreferencesActivity extends AppCompatActivity {
    private RecyclerView prefsView;
    private final String Tag = "Preferable tasks";
    private final String categoriesString = "categories";

    private PrefListAdapter adapter;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<String> categoryNames = new ArrayList<>();
    private ArrayList<Integer> selectedCategories = new ArrayList<>();

    private final FirebaseFirestore db = FirestoreDB.db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        final Button applyButton = findViewById(R.id.apply_button);
        applyButton.setOnClickListener(v -> {
            Map<String, Object> userPref = new HashMap<>();
            userPref.put("categories", selectedCategories);
            db.collection("users").document(user.getUid()).update(userPref);
            Intent intent = new Intent(PreferencesActivity.this, MainActivity.class);
            PreferencesActivity.this.startActivity(intent);
        });
        setupRecyclerView();
    }


    // HAS A BUG WHEN YOU HAVE YO DRAG A SCREEN TO UPDATE VIEW
    private void setupRecyclerView() {
        Log.d("A", "setupRecyclerView");
        prefsView = findViewById(R.id.prefs_recycler_view);
        prefsView.setLayoutManager(new CustomGridLayout(this, 2));
        adapter = new PrefListAdapter(categoryNames, selectedCategories);
        prefsView.setAdapter(adapter);
        fetchNamesFromDB(new CategoriesCallback<String>() {
            @Override
            public void onCallback(List<String> list) {
                categoryNames.addAll(list);
                adapter.notifyDataSetChanged();
            }
        });
        fetchSelectedCategoriesFromDB(new CategoriesCallback<Integer>() {
            @Override
            public void onCallback(List<Integer> list) {
                selectedCategories.addAll(list);
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void fetchSelectedCategoriesFromDB(CategoriesCallback<Integer> callback) {
        ArrayList<Integer> list = new ArrayList<>();
        if (user != null) {
            DocumentReference userProfile = db.collection("users").document(user.getUid());
            userProfile.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Object categories = document.get("categories");
                        if (categories == null) {
                            HashMap<String, Object> field = new HashMap<>();
                            field.put("categories", list);
                            userProfile.update(field);
                        } else {
                            Log.d(Tag, "fetch selected " + categories);
                            callback.onCallback((ArrayList<Integer>)categories);
                        }
                    } else {
                        Log.d(Tag, "No such document");
                    }
                } else {
                    Log.d(Tag, "get failed with ", task.getException());
                }
            });
        }
    }

    private void fetchNamesFromDB(CategoriesCallback<String> callback) {
        db.collection(categoriesString).document(categoriesString).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Object categories = document.get("categories");
                    ArrayList<String> res = (ArrayList<String>) categories;
                    Log.d("Categories", String.valueOf(res));
                    callback.onCallback(res);
                } else {
                    Log.d(Tag, "No such document");
                }
            } else {
                Log.d(Tag, "get failed with ", task.getException());
            }
        });
    }

    private interface CategoriesCallback<T>{
        void onCallback(List<T> list);
    }
}

