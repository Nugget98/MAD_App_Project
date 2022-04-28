package dk.au.mad22spring.app.project.liarsdice.Utilities;

public class User {
    public String UUID;
    public String Displayname;
    public String Wins;
    public String Loses;
    public String TotalGames;

    public User () {};

    public void setDisplayname(String displayname) {
        Displayname = displayname;
    }

    public String getDisplayname(){
        return Displayname;
    }
}
