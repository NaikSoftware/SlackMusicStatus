package ua.naiksoftware.slackmusicstatus.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import ua.naiksoftware.slackmusicstatus.model.User;
import ua.naiksoftware.slackmusicstatus.shared.DataStorage;
import ua.naiksoftware.slackmusicstatus.shared.UserLiveData;

/**
 * Created by naik on 07.08.17.
 */

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<Throwable> errorsLiveData;
    private UserLiveData user;

    public MainViewModel(Application application) {
        super(application);
    }

    public LiveData<User> getUser() {
        if (user == null) {
            user = new UserLiveData(DataStorage.getAccessToken(getApplication()), (MutableLiveData<Throwable>) getErrors());
        }
        return user;
    }

    public LiveData<Throwable> getErrors() {
        if (errorsLiveData == null) errorsLiveData = new MutableLiveData<>();
        return errorsLiveData;
    }
}
