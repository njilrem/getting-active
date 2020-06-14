package com.ga.gettingactive.pref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ga.gettingactive.R;

import java.util.ArrayList;

public class PreferencesActivity extends AppCompatActivity {
    private static final String[] categories = {"health", "beauty", "socialization"};
    private RecyclerView prefsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        prefsView = findViewById(R.id.prefs_recycler_view);
        prefsView.setLayoutManager(new CustomGridLayout(this, 2));
        ArrayList<String> categoryNames = fetchNamesFromDB();
        ArrayList<Integer> selectedCategories = fetchSelectedCategoriesFromDB();
        PrefListAdapter adapter = new PrefListAdapter(categoryNames, selectedCategories);
        prefsView.setAdapter(adapter);
    }

    private ArrayList<Integer> fetchSelectedCategoriesFromDB() {
        //for toha
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        return list;
    }

    private ArrayList<String> fetchNamesFromDB() {
        //for toha
        ArrayList<String> arr = new ArrayList<>();
        for(String s : categories)
            arr.add(s);
        return arr;
    }
}