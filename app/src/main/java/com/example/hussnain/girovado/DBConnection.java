package com.example.hussnain.girovado;

import android.os.StrictMode;
import android.support.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class DBConnection {
    private Connection connection;
    private Statement statement;
    private static String user = "Saeed@serverhuss";
    private static String email = "prosaeed96@gmail.com";
    private static String password = "HussNaiN96";
    DBConnection() throws SQLException, ClassNotFoundException{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        String url = "jdbc:jtds:sqlserver://serverhuss.database.windows.net:1433;DatabaseName=myGiroVadoDatabase;user=Saeed@serverhuss;password=HussNaiN96;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        connection = DriverManager.getConnection(url, user, password);
        statement = connection.createStatement();
    }

    public Statement getStatement() {
        return statement;
    }
    public void close() throws SQLException{
        connection.close();

    }

    public Connection getConnection() {
        return connection;
    }
}
