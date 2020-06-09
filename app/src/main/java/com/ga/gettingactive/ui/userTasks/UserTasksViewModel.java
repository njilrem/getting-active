package com.ga.gettingactive.ui.userTasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserTasksViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserTasksViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}