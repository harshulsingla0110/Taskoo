package com.harshul.taskoo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.harshul.taskoo.adapter.TaskRecyclerViewAdapter;
import com.harshul.taskoo.model.Task;
import com.harshul.taskoo.model.TaskViewModel;

import java.util.List;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private TaskRecyclerViewAdapter mAdapter;
    private Context mContext;
    private List<Task> taskList;


    public SwipeToDeleteCallback(Context context, TaskRecyclerViewAdapter adapter, List<Task> taskList) {
        super(0, ItemTouchHelper.LEFT);
        mAdapter = adapter;
        mContext = context;
        this.taskList = taskList;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            TaskViewModel.deleteTask(taskList.get(position));
        }
    }


    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        ColorDrawable background = new ColorDrawable(mContext.getColor(R.color.main_color));
        int backgroundCornerOffset = 20;

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            Paint paint = new Paint();

            Drawable icon;
            icon = ContextCompat.getDrawable(mContext, R.drawable.ic_delete);
            assert icon != null;
            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            if (dX > 0) {

                background.setBounds(itemView.getLeft(), itemView.getTop() + backgroundCornerOffset, itemView.getLeft() + ((int) dX) + 2 * backgroundCornerOffset, itemView.getBottom() - backgroundCornerOffset);

            } else if (dX < 0) {
                // Swiping to the left
                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getRight() + ((int) dX) - 2 * backgroundCornerOffset, itemView.getTop() + backgroundCornerOffset, itemView.getRight(), itemView.getBottom() - backgroundCornerOffset);

            } else {
                // view is unSwiped
                background.setBounds(0, 0, 0, 0);
            }

            background.draw(canvas);
            icon.draw(canvas);
        }

        viewHolder.itemView.setTranslationX(dX);
    }
}
