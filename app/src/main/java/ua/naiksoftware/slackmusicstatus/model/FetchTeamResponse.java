package ua.naiksoftware.slackmusicstatus.model;

/**
 * Created by naik on 24.06.17.
 */

public class FetchTeamResponse {

    public boolean ok;
    public Team team;

    public static class Team {

        public String name;
        public Icon icon;

        public static class Icon {

            public String image_132;
        }
    }
}
