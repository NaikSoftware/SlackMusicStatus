package ua.naiksoftware.slackmusicstatus;

/**
 * Created by naik on 24.06.17.
 */

public class Config {


    public static class Slack {

        private static final String CLIENT_ID = BuildConfig.SLACK_CLIENT_ID;
        private static final String CLIENT_SECRET = BuildConfig.SLACK_CLIENT_SECRET;
        private static final String API_URL = "https://slack.com/api/";

        public static final String AUTH_URL = "https://slack.com/oauth/authorize?" +
                "client_id=" + CLIENT_ID +
                "&scope=team:read users.profile:read users.profile:write";

        public static final String FETCH_TOKEN_URL = API_URL + "oauth.access?" +
                "client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&code=%s";

        public static final String FETCH_PROFILE_URL = API_URL + "users.profile.get?" +
                "token=%s";

        public static final String FETCH_TEAM_URL = API_URL + "team.info?" +
                "token=%s";

        public static final String POST_STATUS_URL = API_URL + "users.profile.set";

        public static final String REDIRECT_URL_HOST = "localhost";

        public static final String MUSICAL_STATUS_EMOJI = ":musical_note:";
    }
}
