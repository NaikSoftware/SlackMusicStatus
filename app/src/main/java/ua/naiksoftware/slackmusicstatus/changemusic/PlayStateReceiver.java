package ua.naiksoftware.slackmusicstatus.changemusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ua.naiksoftware.slackmusicstatus.Config;
import ua.naiksoftware.slackmusicstatus.shared.DataStorage;
import ua.naiksoftware.slackmusicstatus.R;

public class PlayStateReceiver extends BroadcastReceiver {

    private static final String TAG = PlayStateReceiver.class.getSimpleName();

    private static final int PLAYER_STATE_EMPTY = -1;
    private static final int PLAYER_STATE_PLAY = 3;
    private static final int PLAYER_STATE_STOPPED = 4;
    private static final int PLAYER_STATE_SEEK = 5;

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean playing = intent.getBooleanExtra("playing", false);
        int playerState = intent.getIntExtra("playerState", PLAYER_STATE_EMPTY);

        Log.d(TAG, "playerState = " + playerState + " playing = " + playing);

        if (playerState == PLAYER_STATE_PLAY) {

            String artist = intent.getStringExtra("artist");
            String track = intent.getStringExtra("track");
            if (artist == null) artist = context.getString(R.string.unknown_artist);
            if (track == null) track = context.getString(R.string.unknown_track);

            ChangeStatusService.startHandleChange(context, artist + " - " + track,
                    Config.Slack.MUSICAL_STATUS_EMOJI, false);

        } else if (playerState == PLAYER_STATE_STOPPED || (playerState == PLAYER_STATE_EMPTY && !playing)) {

            ChangeStatusService.startHandleChange(context, DataStorage.getDefaultStatusString(),
                    DataStorage.getDefaultStatusIcon(), false);
        }
    }
}
