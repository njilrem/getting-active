package com.ga.gettingactive.pref;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ga.gettingactive.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PrefListAdapter extends RecyclerView.Adapter<PrefListAdapter.PrefListViewHolder> {
    private final ArrayList<String> categoryNames;
    private final ArrayList<Long> selectedCategories;
    private final AppCompatActivity context;

    public PrefListAdapter(AppCompatActivity context, ArrayList<String> categoryNames, ArrayList<Long> selectedCategories) {
        this.categoryNames = categoryNames;
        this.selectedCategories = selectedCategories;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PrefListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pref_viewholder, parent, false);
        return new PrefListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrefListViewHolder holder, int position) {
        boolean active = selectedCategories.contains((long) position);
        holder.bind(categoryNames.get(position), position, active);
    }

    @Override
    public int getItemCount() {
        return categoryNames.size();
    }

    class PrefListViewHolder extends RecyclerView.ViewHolder {
        private final Button button;
        private boolean activated = false;

        public PrefListViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.category_button);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void bind(String text, int categoryID, boolean active) {
            activated = active;
            button.setText(text);
            if (active) {
                button.setBackgroundColor(context.getResources().getColor(R.color.material_green_500));
            } else {
                button.setBackgroundColor(context.getResources().getColor(R.color.grey));
            }
            button.setOnTouchListener((v, event) -> {

                // show interest in events resulting from ACTION_DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) return true;
                // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                if (event.getAction() != MotionEvent.ACTION_UP) return false;
                if (activated) {
                    selectedCategories.remove((long) categoryID);
                    button.setBackgroundColor(context.getResources().getColor(R.color.grey));

                } else {
                    selectedCategories.add((long) categoryID);
                    button.setBackgroundColor(context.getResources().getColor(R.color.material_green_500));

                }
                activated = !activated;
                //button.setPressed(activated);
                Log.d("ARRAYLIST_STATUS", Arrays.toString(selectedCategories.toArray()));
                return true;
            });
        }
    }
}


