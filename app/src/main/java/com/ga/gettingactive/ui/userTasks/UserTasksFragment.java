package com.ga.gettingactive.ui.userTasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ga.gettingactive.FirestoreDB;
import com.ga.gettingactive.R;
import com.ga.gettingactive.tasklist.TaskAdapter;
import com.ga.gettingactive.tasklist.TaskContainer;
import com.ga.gettingactive.tasklist.TaskListDecorator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserTasksFragment extends Fragment {

    private static final String UserTasksKey = "USER_TASKS";
    private final String TAG = "UserTasksFragment";
    private TaskAdapter taskAdapter;
    private View root;
    private TextView textView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        UserTasksViewModel userTasksViewModel = new ViewModelProvider(this).get(UserTasksViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.ongoing_tasks_list);
        recyclerView.addItemDecoration(new TaskListDecorator((int) (root.getResources().getDimension(R.dimen.tasks_list_margin))));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskAdapter = new TaskAdapter(getString(R.string.task_accepted));
        recyclerView.setAdapter(taskAdapter);
        if (savedInstanceState == null) {
            Log.d(TAG, "Calling from db!");
            setupOngoingTasksView();
        } else {
            Log.d(TAG, "No db call was performed! Saved instance used instead!");
            taskAdapter.setItems(savedInstanceState.getParcelableArrayList(UserTasksKey));
        }

        return root;
    }

    private void setupOngoingTasksView() {

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
                            if(tasks.isEmpty()){
                                textView = root.findViewById(R.id.noTasksTextViewHome);
                                textView.setText("Ви поки що не маєте завдань. Почекайте.");
                            }
                            taskAdapter.setItems(tasks);
                        } else {
                            Log.w(TAG, "Error getting documents.", snapshotTask.getException());
                        }
                    });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(taskAdapter == null) {
            Log.d(TAG, "task adapter is NULL!");
            outState = null;
        }
        else
        outState.putParcelableArrayList(UserTasksKey, taskAdapter.getTasksList());
    }
}