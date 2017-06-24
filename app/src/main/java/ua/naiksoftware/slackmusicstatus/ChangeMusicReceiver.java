package ua.naiksoftware.slackmusicstatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ChangeMusicReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ChangeMusicService.startHandleChange(context,
                intent.getStringExtra("artist"),
                intent.getStringExtra("track"));
    }
}
