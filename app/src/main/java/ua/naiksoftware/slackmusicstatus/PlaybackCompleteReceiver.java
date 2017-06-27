package ua.naiksoftware.slackmusicstatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PlaybackCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ChangeStatusService.startHandleChange(context, DataStorage.getDefaultStatusString(),
                DataStorage.getDefaultStatusIcon(), false);
    }
}
