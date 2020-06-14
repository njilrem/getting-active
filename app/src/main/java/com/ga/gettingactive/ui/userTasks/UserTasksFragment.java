package com.ga.gettingactive.ui.userTasks;

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

import com.ga.gettingactive.FirestoreDB;
import com.ga.gettingactive.R;
import com.ga.gettingactive.TaskAdapter;
import com.ga.gettingactive.TaskContainer;
import com.ga.gettingactive.TaskListDecorator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserTasksFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private TaskAdapter taskAdapter;
    private View root;
    private final String TAG = "UserTasksFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        UserTasksViewModel userTasksViewModel = new ViewModelProvider(this).get(UserTasksViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        setupOngoingTasksView();

        return root;
    }

    private void setupOngoingTasksView() {
        RecyclerView recyclerView = root.findViewById(R.id.ongoing_tasks_list);
        recyclerView.addItemDecoration(new TaskListDecorator((int) (root.getResources().getDimension(R.dimen.tasks_list_margin))));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<TaskContainer> tasks = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DocumentReference userProfile = FirestoreDB.db.collection("users").document(user.getUid());
            userProfile.collection("tasks")
                    .get()
                    .addOnCompleteListener(snapshotTask -> {
                        if (snapshotTask.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(snapshotTask.getResult())) {
                                TaskContainer task = document.toObject(TaskContainer.class);
                                tasks.add(task);
                            }
                            taskAdapter = new TaskAdapter();
                            recyclerView.setAdapter(taskAdapter);
                            taskAdapter.setItems(tasks);
                        } else {
                            Log.w(TAG, "Error getting documents.", snapshotTask.getException());
                        }
                    });
        }
    }

}