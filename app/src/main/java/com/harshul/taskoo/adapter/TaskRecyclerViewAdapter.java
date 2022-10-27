package com.harshul.taskoo.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.harshul.taskoo.R;
import com.harshul.taskoo.databinding.TodoRowBinding;
import com.harshul.taskoo.model.Priority;
import com.harshul.taskoo.model.Task;
import com.harshul.taskoo.model.TaskViewModel;
import com.harshul.taskoo.util.Utils;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

    private static final int TASK_DELETE_TIME = 500;
    private final List<Task> taskList;
    private final OnTodoClickListner todoClickListner;
    private final Context context;
    private TodoRowBinding binding;

    public TaskRecyclerViewAdapter(Context context, List<Task> taskList, OnTodoClickListner todoClickListner) {
        this.context = context;
        this.taskList = taskList;
        this.todoClickListner = todoClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row, parent, false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.todo_row, parent, false);
        View view = binding.getRoot();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        binding.tvTask.setText(task.getTask());
        binding.tvDate.setText(Utils.formatDate(task.getDueDate(), "E, dd MMMM yy"));
        if (task.getDesc() != null)
            binding.tvTaskDesc.setText(task.getDesc());

        String date = Utils.formatDate(task.getDueDate(), "yyyy-MM-dd");
        if (Utils.isToday(date)) {
            holder.tvDay.setText(R.string.today);
            holder.tvDay.setVisibility(View.VISIBLE);
        } else {
            holder.tvDay.setVisibility(View.GONE);
        }

        if (task.getPriority() == Priority.HIGH)
            binding.viewVertical.setAlpha(1.0f);
        else if (task.getPriority() == Priority.MEDIUM)
            binding.viewVertical.setAlpha(0.6f);
        else if (task.getPriority() == Priority.LOW)
            binding.viewVertical.setAlpha(0.2f);

        holder.cvTaskDone.setOnClickListener(view -> {

            if (!holder.textViewTask.getPaint().isStrikeThruText()) {
                holder.textViewTask.setTextColor(context.getColor(R.color.grey_mid_2));
                holder.textViewTask.setPaintFlags(holder.textViewTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.cvTaskDone.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.main_color)));
                holder.cvDone.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.main_color)));
                holder.cvDone.setAlpha(1.0f);
                holder.ivTick.setVisibility(View.VISIBLE);
                holder.lottieDone.playAnimation();
                holder.lottieDone.setVisibility(View.VISIBLE);
                holder.cvTaskDone.setClickable(false);

                new Handler().postDelayed(() -> {
                    TaskViewModel.deleteTask(taskList.get(position));
                }, TASK_DELETE_TIME);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewTask;
        private final TextView tvDay;
        private final CardView cvTaskDone;
        private final CardView cvDone;
        private final ImageView ivTick;
        private final LottieAnimationView lottieDone;
        OnTodoClickListner onTodoClickListner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTask = itemView.findViewById(R.id.tvTask);
            tvDay = itemView.findViewById(R.id.tvDay);
            cvTaskDone = itemView.findViewById(R.id.cvTaskDone);
            cvDone = itemView.findViewById(R.id.cvDone);
            ivTick = itemView.findViewById(R.id.ivTick);
            lottieDone = itemView.findViewById(R.id.lottie_done);
            this.onTodoClickListner = todoClickListner;

            binding.cvTodoItem.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            Task currTask = taskList.get(getAdapterPosition());
            if (id == R.id.cvTodoItem) {
                onTodoClickListner.onTodoClick(getAdapterPosition(), currTask);
            }

        }
    }
}
