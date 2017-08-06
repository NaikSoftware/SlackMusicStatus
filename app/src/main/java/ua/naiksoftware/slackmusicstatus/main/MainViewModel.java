package ua.naiksoftware.slackmusicstatus.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import ua.naiksoftware.slackmusicstatus.model.User;

/**
 * Created by naik on 07.08.17.
 */

public class MainViewModel extends ViewModel {

    private MutableLiveData<Bitmap> userAvatar;
    private MutableLiveData<Bitmap> teamAvatar;
    private MutableLiveData<User> user;

    public LiveData<User> getUser() {
        if (user == null) {

        }
        return user;
    }
}
