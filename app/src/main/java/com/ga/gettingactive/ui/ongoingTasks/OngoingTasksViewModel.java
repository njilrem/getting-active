package com.ga.gettingactive.ui.ongoingTasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OngoingTasksViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OngoingTasksViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}