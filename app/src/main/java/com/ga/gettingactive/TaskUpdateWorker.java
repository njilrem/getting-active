package com.ga.gettingactive;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ga.gettingactive.tasklist.TaskContainer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class TaskUpdateWorker extends Worker {

    private interface TaskCallback<T>{
        void onCallback(List<T> list);
    }

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
            ArrayList<Long> preferences = new ArrayList<>();
            TaskCallback<Long> preferenceCallback = preferences::addAll;
            getPreferences(preferenceCallback, userProfile);
            // too much reads to db, better final array
            String[] categories = {"cоціалізація", "розвиток", "спорт", "догляд", "продуктивність", "краса"};
            ArrayList<String> chosenCategories = new ArrayList<>();
            while(preferences.isEmpty()){}
            for(Long idLong: preferences){
                int id = idLong.intValue();
                chosenCategories.add(categories[id]);
            }
            ArrayList<String> tasksBundles = new ArrayList<>();
            tasksBundles.add("firstBundle");
            tasksBundles.add("secondBundle");
            for (String bundle : tasksBundles) {
                FirestoreDB.db.collection("tasks/" + bundle + "/tasks")
                        .get()
                        .addOnCompleteListener(snapshotTask -> {
                            if (snapshotTask.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(snapshotTask.getResult())) {
                                    TaskContainer task = document.toObject(TaskContainer.class);
                                    for (String tag : task.getHashtags()){
                                        if (chosenCategories.contains(tag))
                                            userProfile.collection("tasks").document(document.getId()).set(task);
                                    }
                                }
                            } else {
                                Log.w("taskUpdate", "Error getting documents.", snapshotTask.getException());
                            }
                        });
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "getting_active")
                    .setSmallIcon(R.drawable.bottle)
                    .setContentTitle("Getting Active")
                    .setContentText("Hi! Keep on doing your tasks! Check your new tasks")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            notificationManager.notify(1, builder.build());
        }
        return Result.success();
    }

    private void getPreferences(TaskCallback<Long> callback, DocumentReference userProfile) {
        final ArrayList<Long> preferences = new ArrayList<>();
        userProfile.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                preferences.addAll((ArrayList<Long>) task.getResult().get("categories"));
                callback.onCallback(preferences);
            }
        });
    }

}
