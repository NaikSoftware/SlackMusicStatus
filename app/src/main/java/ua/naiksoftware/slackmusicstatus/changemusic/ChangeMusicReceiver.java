package ua.naiksoftware.slackmusicstatus.changemusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ua.naiksoftware.slackmusicstatus.Config;
import ua.naiksoftware.slackmusicstatus.R;

public class ChangeMusicReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String artist = intent.getStringExtra("artist");
        String track = intent.getStringExtra("track");
        if (artist == null) artist = context.getString(R.string.unknown_artist);
        if (track == null) track = context.getString(R.string.unknown_track);

        ChangeStatusService.startHandleChange(context, artist + " - " + track, Config.Slack.MUSICAL_STATUS_EMOJI, false);
    }
}
