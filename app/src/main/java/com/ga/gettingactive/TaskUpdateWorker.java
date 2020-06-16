package com.ga.gettingactive;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
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
            ArrayList<Integer> preferences = new ArrayList<>();
            TaskCallback<Integer> preferenceCallback = preferences::addAll;
            getPreferences(preferenceCallback, userProfile);
            // too much reads to db, better final array
            String[] categories = {"cоціалізація", "розвиток", "спорт", "догляд", "продуктивність", "краса"};
            ArrayList<String> chosenCategories = new ArrayList<>();
            while(preferences.isEmpty()){}
            for(Integer id: preferences){
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
//            pushNotification();
        }
        return Result.success();
    }
//    private static void pushNotification() {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.bottle)
//                .setContentTitle("Getting Active")
//                .setContentText("")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//    }
//
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("getting_active", "Getting Active", importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

    private void getPreferences(TaskCallback<Integer> callback, DocumentReference userProfile) {
        final ArrayList<Integer> preferences = new ArrayList<>();
        userProfile.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                preferences.addAll((ArrayList<Integer>) task.getResult().get("categories"));
                callback.onCallback(preferences);
            }
        });
    }

}
