package com.ga.gettingactive.ui.tree;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ga.gettingactive.FirestoreDB;
import com.ga.gettingactive.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;


public class TreeFragment extends Fragment {
    private static final int TASKS_PRE_LEVEL = 5;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private int completedTasks;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.fragment_tree, container, false);
        TextView treeText = root.findViewById(R.id.tree_text);
        TextView treeTextSub = root.findViewById(R.id.tree_text_sub);
        ImageView treeImage = root.findViewById(R.id.tree_image);
        if (user != null) {
            String userUID = user.getUid();
            DocumentReference userDocument = FirestoreDB.db.document("users/" + userUID);
            CollectionReference userArchive = userDocument.collection("archive");
            final int[] doneTasksCount = {0};
            userArchive.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    doneTasksCount[0] = Objects.requireNonNull(task.getResult()).size();
                    Log.d("DONE TASK", String.valueOf(doneTasksCount[0]));
                    if (doneTasksCount[0] == 0) {
                        treeText.setText(getString(R.string.progress_string_no_tasks));
                    } else {
                        treeText.setText(getString(R.string.progress_string, doneTasksCount[0]));
                    }
                     int level = determineUserLevel(doneTasksCount[0]);
                    treeText.setText(getString(R.string.level, level));
                    switch (level) {
                        case 0: {
                            treeImage.setVisibility(View.GONE);
                            treeTextSub.setText(getString(R.string.level_tip0));
                            break;
                        }
                        case 1: {
                            treeImage.setImageDrawable(root.getResources().getDrawable(R.drawable.tree1));
                            treeTextSub.setText(getString(R.string.level_tip));

                            break;
                        }
                        case 2: {
                            treeImage.setImageDrawable(root.getResources().getDrawable(R.drawable.tree2));
                            treeTextSub.setText(getString(R.string.level_tip));
                            break;
                        }
                        case 3: {
                            treeImage.setImageDrawable(root.getResources().getDrawable(R.drawable.tree3));
                            treeTextSub.setText(getString(R.string.level_tip));
                            break;
                        }
                        case 4: {
                            treeImage.setImageDrawable(root.getResources().getDrawable(R.drawable.tree4));
                            treeTextSub.setText(getString(R.string.level_tip));
                            break;
                        }
                        case 5: {
                            treeImage.setImageDrawable(root.getResources().getDrawable(R.drawable.tree5));
                            treeTextSub.setText(getString(R.string.level_tip5));
                            break;
                        }
                    }
                }
            });
        }
        return root;
    }

    private int determineUserLevel(int doneTasks) {
        int level = 0;
        for (int i = 0; i < 5; ++i) {
            if (doneTasks > TASKS_PRE_LEVEL * i) {
                ++level;
            }
        }
        return level;
    }

}