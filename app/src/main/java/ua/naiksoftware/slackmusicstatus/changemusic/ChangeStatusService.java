package ua.naiksoftware.slackmusicstatus.changemusic;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import java.util.Arrays;

import ua.naiksoftware.slackmusicstatus.Config;
import ua.naiksoftware.slackmusicstatus.shared.DataStorage;
import ua.naiksoftware.slackmusicstatus.shared.NetworkHelper;
import ua.naiksoftware.slackmusicstatus.model.User;

public class ChangeStatusService extends IntentService {

    private static final String TAG = ChangeStatusService.class.getSimpleName();

    private static final String EXTRA_STATUS = "ua.naiksoftware.slackmusicstatus.extra.STATUS";
    private static final String EXTRA_STATUS_ICON = "ua.naiksoftware.slackmusicstatus.extra.STATUS_ICON";
    private static final String EXTRA_STATUS_FORCE = "ua.naiksoftware.slackmusicstatus.extra.STATUS_FORCE";

    public ChangeStatusService() {
        super("ChangeStatusService");
    }

    public static void startHandleChange(Context context, String status, String icon, boolean force) {
        Intent intent = new Intent(context, ChangeStatusService.class);
        intent.putExtra(EXTRA_STATUS, status);
        intent.putExtra(EXTRA_STATUS_ICON, icon);
        intent.putExtra(EXTRA_STATUS_FORCE, force);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String accessToken = DataStorage.getAccessToken(this);
        if (accessToken == null ||  // Not authenticated or disabled, ignore
                !intent.getBooleanExtra(EXTRA_STATUS_FORCE, false) && !DataStorage.getEnabled(this)) {
            return;
        }

        String status = intent.getStringExtra(EXTRA_STATUS);
        String icon = intent.getStringExtra(EXTRA_STATUS_ICON);

        if (status != null && status.equals(DataStorage.getLastStatus(this))) {
            Log.d(TAG, "Ignore the same status string - " + status);
            return;
        }

        String jsonProfile = "{\"status_text\":\"" + status + "\", \"status_emoji\":\"" + icon + "\"}";
        String result = NetworkHelper.post(String.class, Config.Slack.POST_STATUS_URL, Arrays.asList(
                new Pair<>("token", accessToken),
                new Pair<>("profile", jsonProfile)
        ));
        if (result != null) {
            Log.d(TAG, "Post new status success: " + result);
        } else {
            Log.d(TAG, "Post new status error");
        }
    }
}
