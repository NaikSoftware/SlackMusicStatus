package ua.naiksoftware.slackmusicstatus.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naik on 24.06.17.
 */

public class FetchProfileResponse {

    public boolean ok;
    public Profile profile;

    public static class Profile {
        public String statusText;
        public String statusEmoji;
        public String firstName;
        public String lastName;
        public String email;
        @SerializedName("image_72")
        public String avatar;
    }
}
