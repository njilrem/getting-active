package com.ga.gettingactive.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ga.gettingactive.TaskContainer;
import com.ga.gettingactive.R;
import com.ga.gettingactive.TaskAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TaskAdapter taskAdapter;
    private View root;
    private String TAG = "HomeFragment";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        setupCompelteedTasksView();

        return root;
    }

    private void setupCompelteedTasksView(){
        recyclerView = root.findViewById(R.id.ongoing_tasks_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Collection<TaskContainer> tasks = new ArrayList<>();
        db.collection("tasks/firstBundle/tasks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            TaskContainer taskContainer = document.toObject(TaskContainer.class);
                            Log.d("TASK", String.valueOf(taskContainer));
                            tasks.add(taskContainer);
                            if(!tasks.contains(taskContainer)){
                                Log.d("ERROR TASKS", "MISSING");
                            }else {
                                Log.d("ERROR TASKS", "AVAILEBLE");
                            }
                        }
                        taskAdapter = new TaskAdapter();
                        recyclerView.setAdapter(taskAdapter);
                        taskAdapter.setItems(tasks);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
        Log.d("TASKS", String.valueOf(tasks.size()));
    }

   private void getCompletedTasks(){


    }
}