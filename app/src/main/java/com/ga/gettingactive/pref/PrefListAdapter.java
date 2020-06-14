package com.ga.gettingactive.pref;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ga.gettingactive.R;
import com.ga.gettingactive.tasklist.TaskAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PrefListAdapter extends RecyclerView.Adapter<PrefListAdapter.PrefListViewHolder> {
    private final ArrayList<String> categoryNames;
    private final ArrayList<Integer> selectedCategories;

    public PrefListAdapter(ArrayList<String> categoryNames, ArrayList<Integer> selectedCategories){
        this.categoryNames = categoryNames;
        this.selectedCategories = selectedCategories;
        notifyDataSetChanged();
    }

    private final HashMap<String, Boolean> categoryStatuses = new HashMap<>();
    @NonNull
    @Override
    public PrefListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pref_viewholder, parent, false);
        return new PrefListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrefListViewHolder holder, int position) {
        boolean active = selectedCategories.contains(position);
        holder.bind(categoryNames.get(position), position, active);
    }

    @Override
    public int getItemCount() {
        return categoryNames.size();
    }

    class PrefListViewHolder extends RecyclerView.ViewHolder{
        private Button button;
        private boolean activated;


        public PrefListViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.category_button);
        }

        public void bind(String text, int categoryID,  boolean active){
            button.setText(text);
            activated = active;
            button.setPressed(activated);
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // show interest in events resulting from ACTION_DOWN
                    if (event.getAction() == MotionEvent.ACTION_DOWN) return true;

                    // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                    if (event.getAction() != MotionEvent.ACTION_UP) return false;
                    if(activated){
                        selectedCategories.remove((Integer)categoryID);
                    }else{
                        selectedCategories.add(categoryID);
                    }
                    Log.d("ARRAYLIST_STATUS", Arrays.toString(selectedCategories.toArray()));
                        activated = !activated;
                        button.setPressed(activated);
                        return true;
                }
            });
        }
    }
}


