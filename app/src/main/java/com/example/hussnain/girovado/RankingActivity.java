package com.example.hussnain.girovado;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankingActivity extends AppCompatActivity {

    @BindView(R.id.list)
    ListView list;
    ArrayAdapter<String> ranking_adapter;
    ArrayList<String> rankingArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        ButterKnife.bind(this);
        rankingArrayList = new ArrayList<>();
        try {
            DBConnection dbConnection = new DBConnection();
            String query = "Select * from dbo.User_app order by point desc";
            ResultSet rs = dbConnection.getStatement().executeQuery(query);
            int cont = 1;
            while(rs.next()){
                rankingArrayList.add(cont + ".   " + rs.getString("name") + "\n" + "Point: " + rs.getInt("point"));
                cont++;
            }
            dbConnection.close();
        } catch (SQLException e) { e.printStackTrace(); }
        catch (ClassNotFoundException c) { }
        ranking_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, rankingArrayList);
        list.setAdapter(ranking_adapter);
    }
}
