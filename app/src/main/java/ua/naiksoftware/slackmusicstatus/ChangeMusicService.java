package ua.naiksoftware.slackmusicstatus;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import ua.naiksoftware.slackmusicstatus.model.User;

public class ChangeMusicService extends IntentService {

    private static final String TAG = ChangeMusicService.class.getSimpleName();

    private static final String EXTRA_ARTIST = "ua.naiksoftware.slackmusicstatus.extra.ARTIST";
    private static final String EXTRA_TRACK = "ua.naiksoftware.slackmusicstatus.extra.TRACK";

    public ChangeMusicService() {
        super("ChangeMusicService");
    }

    public static void startHandleChange(Context context, String artist, String track) {
        Intent intent = new Intent(context, ChangeMusicService.class);
        intent.putExtra(EXTRA_ARTIST, artist);
        intent.putExtra(EXTRA_TRACK, track);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        User user = DataStorage.getUser(this);
        if (user == null) return; // Not authenticated, ignore

        String artist = intent.getStringExtra(EXTRA_ARTIST);
        String track = intent.getStringExtra(EXTRA_TRACK);
        if (artist == null) artist = getString(R.string.unknown_artist);
        if (track == null) track = getString(R.string.unknown_track);
        String jsonProfile = "{\"status_text\":\"" + artist + " - " + track + "\", \"status_emoji\":\"" + Config.Slack.MUSICAL_STATUS_EMOJI + "\"}";
        String result = NetworkHelper.post(String.class, Config.Slack.POST_STATUS_URL, Arrays.asList(
                new Pair<>("token", user.token),
                new Pair<>("profile", jsonProfile)
        ));
        if (result != null) {
            Log.d(TAG, "Post new status success: " + result);
        } else {
            Log.d(TAG, "Post new status error");
        }
    }
}
