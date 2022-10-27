package com.harshul.taskoo.adapter;

import com.harshul.taskoo.model.Task;

public interface OnTodoClickListner {


    void onTodoClick(int adapterPosition, Task task);

    void onRadioClick(Task currTask);
}
