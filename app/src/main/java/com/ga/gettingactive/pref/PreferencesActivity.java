package com.ga.gettingactive.pref;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.ga.gettingactive.FirestoreDB;
import com.ga.gettingactive.R;
import com.ga.gettingactive.tasklist.TaskContainer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PreferencesActivity extends AppCompatActivity {
    private static final String[] categories = {"health", "beauty", "socialization"};
    private RecyclerView prefsView;
    private String Tag = "Preferable tasks";

    FirebaseFirestore db = FirestoreDB.db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        setupRecyclerView();
    }


    // HAS A BUG WHEN YOU HAVE YO DRAG A SCREEN TO UPDATE VIEW
    private void setupRecyclerView() {
        Log.d("A", "setupRecyclerView");
        prefsView = findViewById(R.id.prefs_recycler_view);
        prefsView.setLayoutManager(new CustomGridLayout(this, 2));
        ArrayList<String> categoryNames = new ArrayList<>();
        fetchNamesFromDB(new CategoriesCallback<String>() {
            @Override
            public void onCallback(List<String> list) {
                categoryNames.addAll(list);
            }
        });
        Log.d(Tag, String.valueOf(categoryNames));
        ArrayList<Integer> selectedCategories = new ArrayList<>();
        fetchSelectedCategoriesFromDB(new CategoriesCallback<Integer>() {
            @Override
            public void onCallback(List<Integer> list) {
                selectedCategories.addAll(list);
            }
        });
        Log.d(Tag, String.valueOf(selectedCategories));
        PrefListAdapter adapter = new PrefListAdapter(categoryNames, selectedCategories);
        prefsView.setAdapter(adapter);
    }

    private void fetchSelectedCategoriesFromDB(CategoriesCallback<Integer> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
        db.collection("categories").document("categories").get().addOnCompleteListener(task -> {
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

