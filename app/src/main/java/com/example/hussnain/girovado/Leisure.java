package com.example.hussnain.girovado;

import com.darsh.multipleimageselect.models.Image;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

public class Leisure {
    private static int leisureId;
    private static int place;
    private static ArrayList<String> leisureType;
    private static String leisureName;
    private static String leisureAddress;
    private static String leisureCity;
    private static String leisureDescription;
    private static String date;
    private static String time;
    private static String timeToDedicate;
    private static ArrayList<Transport> transports;
    Leisure(int leisureId){
        this.leisureId = leisureId;
    }
    Leisure(int p, ArrayList<String> lt, String ln, String la, String lc, String ld, String d, String t, String tm){
        try{
            Random random = new Random();
            boolean exit = false;
            DBConnection dbConnection = new DBConnection();
            while(exit == false) {
                leisureId = random.nextInt(10000);
                String query = "Select leisureID from dbo.Leisure where leisureID = " + leisureId + " ";
                ResultSet rs = dbConnection.getStatement().executeQuery(query);
                if (rs.next())
                    exit = false;
                else
                    exit = true;
            }
            dbConnection.close();
        }
        catch (SQLException s){
            s.printStackTrace();
        }
        catch(ClassNotFoundException c){
            c.printStackTrace();
        }
        place = p;
        leisureType = lt;
        leisureAddress = la;
        leisureCity = lc;
        leisureDescription = ld;
        leisureName = ln;
        if(p == 1){
            date = null;
            time = null;
        } else {
            date = d;
            time = t;
        }
        timeToDedicate = tm;
        transports = new ArrayList<>();
    }


    public static int getPlace() {
        return place;
    }

    public static int getLeisureId() {
        return leisureId;
    }

    public static String getDate() {
        return date;
    }

    public static String getTimeToDedicate() {
        return timeToDedicate;
    }

    public static String getLeisureAddress() {
        return leisureAddress;
    }

    public static String getLeisureCity() {
        return leisureCity;
    }

    public static String getLeisureDescription() {
        return leisureDescription;
    }

    public static String getLeisureName() {
        return leisureName;
    }

    public static ArrayList<String> getLeisureType() {
        return leisureType;
    }

    public static String getTime() {
        return time;
    }

    public static ArrayList<Transport> getTransports() {
        return transports;
    }

    public static void setLeisureId(int leisureId) {
        Leisure.leisureId = leisureId;
    }

}
