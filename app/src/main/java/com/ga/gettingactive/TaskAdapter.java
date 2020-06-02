package com.ga.gettingactive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskContainer> tasksList = new ArrayList<>();

    public void setItems(Collection<TaskContainer> tasks) {
        tasksList.addAll(tasks);
        notifyDataSetChanged();
    }

    public void clearItems() {
        tasksList.clear();
        notifyDataSetChanged();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
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

    class TaskViewHolder extends RecyclerView.ViewHolder{
        private TextView titleView;
        private TextView descriptionView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.task_title);
            descriptionView = itemView.findViewById(R.id.task_description);
        }

        public void bind(TaskContainer task){
        titleView.setText(task.getTitle());
        descriptionView.setText(task.getDescription());
        }
    }






}


