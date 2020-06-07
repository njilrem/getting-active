package com.ga.gettingactive.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ga.gettingactive.FirestoreDB;
import com.ga.gettingactive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.concurrent.atomic.AtomicReference;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
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
        progressText.setText("You done total of " + doneTasksCount[0] + " tasks.");
        return root;
    }
}