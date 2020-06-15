package com.ga.gettingactive;

import android.os.Bundle;

import com.ga.gettingactive.tasklist.TaskAdapter;
import com.ga.gettingactive.tasklist.TaskContainer;
import com.ga.gettingactive.tasklist.TaskListDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import static com.ga.gettingactive.FirestoreDB.db;

public class ArchiveActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (savedInstanceState == null) {
            setupRecyclerView();
        }
    }

    private void setupRecyclerView() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            recyclerView = findViewById(R.id.completed_tasks_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            DocumentReference userProfile = db.document("users/" + user.getUid());
            userProfile.collection("archive")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            ArrayList<TaskContainer> archivedTasks = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TaskContainer taskContainer = document.toObject(TaskContainer.class);
                                Log.d("TASK", String.valueOf(taskContainer));
                                archivedTasks.add(taskContainer);
                            }
                            if (archivedTasks.isEmpty()) {
                                TextView noOngoingTasksTextView = findViewById(R.id.noTasksTextView);
                                final String text = "You currently have no completed tasks. try to choose some from All Tasks page";
                                noOngoingTasksTextView.setText(text);
                            }
                            TaskAdapter taskAdapter = new TaskAdapter();
                            ;
                            recyclerView.setAdapter(taskAdapter);
                            taskAdapter.setItems(archivedTasks);
                        } else {
                            Log.d("Archive activity", "Error getting documents: ", task.getException());
                        }
                    });
        } else {
            // No user is signed in
            Log.e("LOGIN", "LOGIN ERROR");
        }
    }
}