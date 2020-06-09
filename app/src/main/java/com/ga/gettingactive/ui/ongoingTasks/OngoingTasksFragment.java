package com.ga.gettingactive.ui.ongoingTasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ga.gettingactive.FirestoreDB;
import com.ga.gettingactive.R;
import com.ga.gettingactive.TaskAdapter;
import com.ga.gettingactive.TaskContainer;
import com.ga.gettingactive.TaskListDecorator;
import com.ga.gettingactive.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

public class OngoingTasksFragment extends androidx.fragment.app.Fragment {

    private static final String TAG = "OngoingTasksFragment";
    private OngoingTasksViewModel ongoingTasksViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private final FirebaseFirestore db = FirestoreDB.db;
    private TaskAdapter taskAdapter;
    private View root;
    private final List<TaskContainer> completedTasks = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ongoingTasksViewModel = new ViewModelProvider(this).get(OngoingTasksViewModel.class);
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        if(savedInstanceState == null)
            getOngoingTasks();

        return root;
    }

    private void getOngoingTasks() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            recyclerView = root.findViewById(R.id.ongoing_tasks_list);
            recyclerView.addItemDecoration(new TaskListDecorator((int) (root.getResources().getDimension(R.dimen.tasks_list_margin))));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            String uid = user.getUid();
            String name = user.getDisplayName();
            String firstname = name.split(" ")[0];
            String lastname = name.split(" ")[1];
            User userObj = new User(uid, firstname, lastname);
            DocumentReference userProfile = db.document("users/" + uid);
            userProfile.set(userObj, SetOptions.merge());
            userProfile.collection("myTasks")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TaskContainer taskContainer = document.toObject(TaskContainer.class);
                                Log.d("TASK", String.valueOf(taskContainer));
                                completedTasks.add(taskContainer);
                            }
                            switch (completedTasks.size()){
                                case 0: {
                                    TextView noOngoingTasksTextView = root.findViewById(R.id.noTasksTextView);
                                    final String text = "You currently have no ongoing tasks. try to choose some from All Tasks page";
                                    noOngoingTasksTextView.setText(text);
                                    break;
                                }
                                default: {

                                }
                            }
                            taskAdapter = new TaskAdapter();
                            recyclerView.setAdapter(taskAdapter);
                            taskAdapter.setItems(completedTasks);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } else {
            // No user is signed in
            Log.e("LOGIN", "LOGIN ERROR");
        }
    }
}