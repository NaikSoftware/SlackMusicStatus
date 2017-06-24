package ua.naiksoftware.slackmusicstatus.model;

/**
 * Created by naik on 24.06.17.
 */

public class AuthResponse {

    public final String accessToken;
    public final String teamName;

    public AuthResponse(String accessToken, String teamName) {
        this.accessToken = accessToken;
        this.teamName = teamName;
    }
}
