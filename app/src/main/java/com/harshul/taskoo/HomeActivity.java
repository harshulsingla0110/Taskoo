package com.harshul.taskoo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.harshul.taskoo.adapter.OnTodoClickListner;
import com.harshul.taskoo.adapter.TaskRecyclerViewAdapter;
import com.harshul.taskoo.databinding.ActivityHomeBinding;
import com.harshul.taskoo.model.SharedViewModel;
import com.harshul.taskoo.model.Task;
import com.harshul.taskoo.model.TaskViewModel;
import com.harshul.taskoo.util.Constants;
import com.harshul.taskoo.util.Utils;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomeActivity extends AppCompatActivity implements OnTodoClickListner {

    private final Activity mActivity = HomeActivity.this;
    private ActivityHomeBinding binding;
    private TaskRecyclerViewAdapter recyclerViewAdapter;
    private BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_home);
        Utils.bottomBarColor(mActivity);
        context = this;

        bottomSheetFragment = new BottomSheetFragment();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        setUserData();


        TaskViewModel taskViewModel = new ViewModelProvider.AndroidViewModelFactory(mActivity.getApplication()).create(TaskViewModel.class);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        binding.clBottomBar.setOnClickListener(view -> {
            showBottomSheetDialog();
        });

        binding.textViewHeading.setOnClickListener(view -> detailsPopup());
        binding.profileImage.setOnClickListener(view -> detailsPopup());

        taskViewModel.getAllTasks().observe(this, tasks -> {
            recyclerViewAdapter = new TaskRecyclerViewAdapter(mActivity, tasks, this);
            binding.recyclerView.setAdapter(recyclerViewAdapter);
            if (tasks.size() == 0) {
                binding.groupNoTask.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
                binding.tvTaskPending.setText(R.string.no_task);
            } else {
                binding.groupNoTask.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                if (tasks.size() == 1)
                    binding.tvTaskPending.setText(R.string.one_task);
                else
                    binding.tvTaskPending.setText(MessageFormat.format("{0} tasks pending", tasks.size()));


            }
        });

    }

    private void setUserData() {
        String name = Utils.getInstance().getFromSharedPreference(mActivity, Constants.NAME);
        if (name != null && !TextUtils.isEmpty(name))
            binding.textViewHeading.setText(MessageFormat.format("Hi, {0} \uD83D\uDC4B", name.split(" ")[0]));
        else
            binding.textViewHeading.setText(Utils.textColor(mActivity, getString(R.string.hello), R.color.light_grey, 4, 7));

        boolean isMale = Boolean.parseBoolean(Utils.getInstance().getFromSharedPreference(mActivity, Constants.IS_MALE));
        if (isMale)
            Glide.with(getApplicationContext()).load(R.drawable.profile_pic_male).into(binding.profileImage);
        else
            Glide.with(getApplicationContext()).load(R.drawable.profile_pic_female).into(binding.profileImage);

    }


    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    private void detailsPopup() {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.details_popup);
        dialog.setCancelable(true);

        ImageView ivMale = dialog.findViewById(R.id.ivMale);
        ImageView ivFemale = dialog.findViewById(R.id.ivFemale);
        EditText etName = dialog.findViewById(R.id.etName);
        Button buttonSave = dialog.findViewById(R.id.buttonProceed);

        String userName = Utils.getInstance().getFromSharedPreference(context, Constants.NAME);
        if (userName != null && !TextUtils.isEmpty(userName))
            etName.setText(userName);

        AtomicBoolean isMale = new AtomicBoolean(true);
        isMale.set(Boolean.parseBoolean(Utils.getInstance().getFromSharedPreference(context, Constants.IS_MALE)));
        if (isMale.get())
            genderColor(ivMale, ivFemale);
        else
            genderColor(ivFemale, ivMale);


        ivMale.setOnClickListener(view -> {
            genderColor(ivMale, ivFemale);
            isMale.set(true);
        });
        ivFemale.setOnClickListener(view -> {
            genderColor(ivFemale, ivMale);
            isMale.set(false);
        });
        buttonSave.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            if (!TextUtils.isEmpty(name)) {
                Utils.getInstance().saveToSharedPreference(context, Constants.NAME, name);
                Utils.getInstance().saveToSharedPreference(context, Constants.IS_MALE, isMale);
                dialog.dismiss();
                setUserData();
            } else {
                Toast.makeText(mActivity, "Please enter your name", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(width - 100, ActionBar.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void genderColor(ImageView selectedGender, ImageView otherGender) {
        selectedGender.setImageTintList(getColorStateList(R.color.main_color));
        otherGender.setImageTintList(getColorStateList(R.color.black));
    }

    @Override
    public void onTodoClick(int adapterPosition, Task task) {
        sharedViewModel.selectItem(task);
        sharedViewModel.setEdit(true);
        showBottomSheetDialog();
    }

    @Override
    public void onRadioClick(Task currTask) {
        TaskViewModel.deleteTask(currTask);
        recyclerViewAdapter.notifyDataSetChanged();
    }

}