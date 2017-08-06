package ua.naiksoftware.slackmusicstatus.changemusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ua.naiksoftware.slackmusicstatus.shared.DataStorage;

public class PlaybackCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ChangeStatusService.startHandleChange(context, DataStorage.getDefaultStatusString(),
                DataStorage.getDefaultStatusIcon(), false);
    }
}
