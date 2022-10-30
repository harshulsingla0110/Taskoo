package com.harshul.taskoo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.harshul.taskoo.databinding.ActivityMainBinding;
import com.harshul.taskoo.model.Task;
import com.harshul.taskoo.model.TaskViewModel;
import com.harshul.taskoo.util.Constants;
import com.harshul.taskoo.util.Utils;

import java.text.MessageFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final Activity mActivity = MainActivity.this;
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private ActivityMainBinding binding;
    private TaskViewModel taskViewModel;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_main);

        binding.tvVersion.setText(MessageFormat.format("V:{0}", Utils.getVersionName(mActivity)));

        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(mActivity.getApplication()).create(TaskViewModel.class);

        ForegroundColorSpan color = new ForegroundColorSpan(getColor(R.color.main_color));
        SpannableString spannableString = new SpannableString(getString(R.string.splash_screen_heading));
        spannableString.setSpan(color, 35, 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, 35, 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.textViewHeading.setText(spannableString);


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.WHITE);
        View decor = window.getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        taskViewModel.getAllTasks().observe(this, tasks -> {
            taskList = tasks;
        });

        new Handler().postDelayed(() -> {
            Boolean haveTasks = false;
            if (taskList.size() != 0) {
                haveTasks = true;
                //binding.buttonProceed.setVisibility(View.GONE);
            }

            //binding.buttonProceed.setVisibility(View.VISIBLE);
            Intent intent = new Intent(mActivity, HomeActivity.class);
            intent.putExtra(Constants.HAVE_TASKS, haveTasks);
            startActivity(intent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);

    }
}