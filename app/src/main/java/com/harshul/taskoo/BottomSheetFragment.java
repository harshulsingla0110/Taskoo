package com.harshul.taskoo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.harshul.taskoo.databinding.BottomSheetBinding;
import com.harshul.taskoo.model.Priority;
import com.harshul.taskoo.model.SharedViewModel;
import com.harshul.taskoo.model.Task;
import com.harshul.taskoo.model.TaskViewModel;
import com.harshul.taskoo.util.Utils;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    Calendar calendar = Calendar.getInstance();
    private BottomSheetBinding binding;
    private Date dueDate;
    private SharedViewModel sharedViewModel;
    private Boolean isEdit = false;
    private Priority priority = Priority.HIGH;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel.getSelectedItem().getValue() != null && sharedViewModel.getEdit()) {
            isEdit = sharedViewModel.getEdit();
            Task task = sharedViewModel.getSelectedItem().getValue();
            dueDate = task.getDueDate();
            priority = task.getPriority();
            binding.etTodo.setText(task.getTask());
            binding.enterTodoDesc.setText(task.getDesc());
            binding.calendarView.setDate(Utils.getDate(task.getDueDate()), true, true);

            if (task.getPriority() == Priority.HIGH)
                setPriority(binding.tvHigh, binding.tvMedium, binding.tvLow);
            else if (task.getPriority() == Priority.MEDIUM)
                setPriority(binding.tvMedium, binding.tvHigh, binding.tvLow);
            else if (task.getPriority() == Priority.LOW)
                setPriority(binding.tvLow, binding.tvHigh, binding.tvMedium);
        } else {
            binding.etTodo.setText("");
            binding.enterTodoDesc.setText("");
            priority = Priority.HIGH;
            dueDate = Calendar.getInstance().getTime();
            binding.calendarView.setDate(Utils.getDate(dueDate), true, true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isEdit = false;
        sharedViewModel.setEdit(false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding.buttonProceed.setOnClickListener(v -> {
            String task = binding.etTodo.getText().toString().trim();
            String taskDesc = binding.enterTodoDesc.getText().toString().trim();

            if (!TextUtils.isEmpty(task) && dueDate != null) {
                if (isEdit) {
                    Task updatedTask = sharedViewModel.getSelectedItem().getValue();
                    assert updatedTask != null;
                    updatedTask.setTask(task);
                    updatedTask.setCreatedAt(Calendar.getInstance().getTime());
                    updatedTask.setPriority(priority);
                    updatedTask.setDueDate(dueDate);
                    updatedTask.setDesc(taskDesc);
                    TaskViewModel.updateTask(updatedTask);
                    sharedViewModel.setEdit(false);
                } else {
                    Task newTask = new Task(task, priority, dueDate, Calendar.getInstance().getTime(), false, taskDesc);
                    TaskViewModel.insertTask(newTask);
                }
                if (this.isVisible()) {
                    this.dismiss();
                }
            } else {
                Toast.makeText(getContext(), R.string.enter_task, Toast.LENGTH_SHORT).show();
            }
        });

        binding.calendarView.setOnDateChangeListener((calenderView, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMonth);
            dueDate = calendar.getTime();
        });

        binding.calendarView.setMinDate(System.currentTimeMillis());

        binding.tvHigh.setOnClickListener(v -> {
            setPriority(binding.tvHigh, binding.tvMedium, binding.tvLow);
            priority = Priority.HIGH;
        });

        binding.tvMedium.setOnClickListener(v -> {
            setPriority(binding.tvMedium, binding.tvHigh, binding.tvLow);
            priority = Priority.MEDIUM;
        });

        binding.tvLow.setOnClickListener(v -> {
            setPriority(binding.tvLow, binding.tvHigh, binding.tvMedium);
            priority = Priority.LOW;
        });
    }

    private void setPriority(TextView tv1, TextView tv2, TextView tv3) {
        tv1.setTextColor(requireContext().getColor(R.color.white));
        tv1.setBackgroundColor(requireContext().getColor(R.color.main_color));
        tv2.setTextColor(requireContext().getColor(R.color.black));
        tv2.setBackgroundColor(requireContext().getColor(R.color.light_grey));
        tv3.setTextColor(requireContext().getColor(R.color.black));
        tv3.setBackgroundColor(requireContext().getColor(R.color.light_grey));
    }
}