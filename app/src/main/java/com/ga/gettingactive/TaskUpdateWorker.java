package com.ga.gettingactive;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class TaskUpdateWorker extends Worker {

    public TaskUpdateWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }


    @NonNull
    @Override
    public Result doWork() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DocumentReference userProfile = FirestoreDB.db.collection("users").document(user.getUid());
            FirestoreDB.db.collection("tasks/secondBundle/tasks")
                    .get()
                    .addOnCompleteListener(snapshotTask -> {
                        if (snapshotTask.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(snapshotTask.getResult())) {
                                TaskContainer task = document.toObject(TaskContainer.class);
                                userProfile.collection("tasks").document(document.getId()).set(task);
                            }
                        } else {
                            Log.w("taskUpdate", "Error getting documents.", snapshotTask.getException());
                        }
                    });
        }
        return Result.success();
    }
}
