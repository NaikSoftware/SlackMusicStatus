package ua.naiksoftware.slackmusicstatus.shared;

import android.arch.lifecycle.LiveData;

import ua.naiksoftware.slackmusicstatus.model.User;

/**
 * Created by naik on 07.08.17.
 */

public class UserLiveData extends LiveData<User> {
    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }
}
