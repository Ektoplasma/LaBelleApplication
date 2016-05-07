package com.ektoplasma.valou.nevalost;

/**
 * Created by ektoplasma on 07/05/16.
 */
public final class Profile {

    private static String username;
    private static String pursuit;
    private static String cookieInstance;
    /*On pourra rajouter d'autres informations comme la geol initiale, un avatar, etc...*/

    public static String getUsername() { return username; }

    public static void setUsername(String username) { Profile.username = username; }

    public static String getPursuit() { return pursuit; }

    public static void setPursuit(String pursuit) { Profile.pursuit = pursuit; }

    public static String getCookieInstance() { return cookieInstance; }

    public static void setCookieInstance(String cookieInstance) { Profile.cookieInstance = cookieInstance; }
}
