package com.harshul.taskoo.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<Task> selectedItem = new MutableLiveData<>();
    private Boolean isEdit;

    public void selectItem(Task task) {
        selectedItem.setValue(task);
    }

    public LiveData<Task> getSelectedItem() {
        return selectedItem;
    }

    public Boolean getEdit() {
        return isEdit;
    }

    public void setEdit(Boolean edit) {
        this.isEdit = edit;
    }
}
