package com.ga.gettingactive.ui.profile;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private View root;
    private final FirebaseFirestore db = FirestoreDB.db;
    private TaskAdapter taskAdapter;
    private final String TAG = "PROFILE";
    private final List<TaskContainer> completedTasks = new ArrayList<>();

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView usernameText = root.findViewById(R.id.text_username);
        final TextView progressText = root.findViewById(R.id.progress_text);
        //NAME
        String name = user.getDisplayName();
        String userUID = user.getUid();
        DocumentReference userDocument = FirestoreDB.db.document("users/" + userUID);
        CollectionReference userArchive = userDocument.collection("archive");
        final int[] doneTasksCount = {0};
        userArchive.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                doneTasksCount[0] = task.getResult().size();
                Log.d("DONE TASK", String.valueOf(doneTasksCount[0]));
            }
        });
        usernameText.setText(name);
        progressText.setText(getString(R.string.progress_string, doneTasksCount[0]));
        return root;
    }

    private void getCompletedTasks() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            RecyclerView recyclerView = root.findViewById(R.id.ongoing_tasks_list);
            recyclerView.addItemDecoration(new TaskListDecorator((int) (root.getResources().getDimension(R.dimen.tasks_list_margin))));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            String uid = user.getUid();
            DocumentReference userProfile = db.document("users/" + uid);
            userProfile.collection("archive")
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