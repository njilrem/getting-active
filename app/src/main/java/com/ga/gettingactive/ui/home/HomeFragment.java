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

import com.ga.gettingactive.R;
import com.ga.gettingactive.TaskAdapter;
import com.ga.gettingactive.TaskContainer;
import com.ga.gettingactive.TaskListDecorator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TaskAdapter taskAdapter;
    private View root;
    private final String TAG = "HomeFragment";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userProfile;
    private List<TaskContainer> completedTasks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        setupOngoingTasksView();

        return root;
    }

    private void setupOngoingTasksView() {
        recyclerView = root.findViewById(R.id.ongoing_tasks_list);
        recyclerView.addItemDecoration(new TaskListDecorator((int) (root.getResources().getDimension(R.dimen.tasks_list_margin))));
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
                            if (!tasks.contains(taskContainer)) {
                                Log.d("ERROR TASKS", "MISSING");
                            } else {
                                Log.d("ERROR TASKS", "AVAILABLE");
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


    //TODO ADD TO VIEW
    private void getCompletedTasks() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            userProfile = db.document("users/" + uid);
            userProfile.collection("archive")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TaskContainer taskContainer = document.toObject(TaskContainer.class);
                                Log.d("TASK", String.valueOf(taskContainer));
                                completedTasks.add(taskContainer);
                                if (!completedTasks.contains(taskContainer)) {
                                    Log.d("ERROR TASKS", "MISSING");
                                } else {
                                    Log.d("ERROR TASKS", "AVaILaBLE");
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } else {
            // No user is signed in
        }
    }
}