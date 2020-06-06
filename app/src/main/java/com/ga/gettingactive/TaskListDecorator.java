package com.ga.gettingactive;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//(private val spaceHeight: Int)
public class TaskListDecorator extends RecyclerView.ItemDecoration{
    private final int margin;
    public TaskListDecorator(int margin){
        this.margin = margin;
    }
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = margin;
        }
        outRect.left = margin;
        outRect.right = margin;
        outRect.bottom = margin;
    }
}