package com.ga.gettingactive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<TaskContainer> tasksList = new ArrayList<>();


    public void setItems(Collection<TaskContainer> tasks) {
        tasksList.addAll(tasks);
        notifyDataSetChanged();
    }

    public void clearItems() {
        tasksList.clear();
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_viewholder, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(tasksList.get(position));
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder{
        private final TextView titleView;
        private final TextView descriptionView;
        private final TextView tipView;
        private final TextView tagsView;
        private final Button button;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.task_title);
            descriptionView = itemView.findViewById(R.id.task_description);
            tipView = itemView.findViewById(R.id.tip_text);
            tagsView = itemView.findViewById(R.id.tags_text);
            button = itemView.findViewById(R.id.button);
        }

        public void bind(TaskContainer task){
            titleView.setText(task.getTitle());
            descriptionView.setText(task.getDescription());
            tipView.setText(task.getTip());
            tagsView.setText(task.getHashtags());
            button.setOnClickListener(v -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();
                    String name = user.getDisplayName();
                    String firstname = name.split(" ")[0];
                    String lastname = name.split(" ")[1];
                    User userObj = new User(uid, firstname, lastname);
                    DocumentReference userProfileDoc = FirestoreDB.db.document("users/" + uid);
                    userProfileDoc.set(userObj, SetOptions.merge());
                    userProfileDoc.collection("myTasks").add(task);
                }
            });
        }
    }






}


