package com.example.hussnain.girovado;

public class User {
    private static String name;
    private static String userID;
    private static String password;
    private static Boolean connected;

    User(String name, String userID, String password, Boolean connected){
        this.name = name;
        this.userID = userID;
        this.password = password;
        this.connected = connected;
    }

    public static String getName() {
        return name;
    }

    public static String getPassword() {
        return password;
    }

    public static Boolean getConnected() {
        return connected;
    }

    public static String getUserID() {
        return userID;
    }
}
