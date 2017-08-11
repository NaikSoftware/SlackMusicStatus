package ua.naiksoftware.slackmusicstatus.model;

import android.graphics.Bitmap;

/**
 * Created by naik on 23.06.17.
 */

public class User {

    public final String token;
    public final String name;
    public final String avatar;
    public final String teamName;
    public final String teamAvatar;
    public Bitmap userAvatarBitmap;
    public Bitmap teamAvatarBitmap;

    public User(String token, String name, String avatar, String teamName, String teamAvatar) {
        this.token = token;
        this.name = name;
        this.avatar = avatar;
        this.teamName = teamName;
        this.teamAvatar = teamAvatar;
    }
}
