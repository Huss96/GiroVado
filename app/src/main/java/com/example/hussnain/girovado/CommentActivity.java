package com.example.hussnain.girovado;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentActivity extends AppCompatActivity {
    @BindView(R.id.listComment) ListView list;
    @BindView(R.id.textComment) EditText comment;
    @BindView(R.id.buttonComment) Button commentSave;
    @BindView(R.id.commBack) Button back;
    ArrayAdapter<String> comment_adapter;
    ArrayList<String> commentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        init();
        list.setAdapter(comment_adapter);
        commentSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User.getConnected()== false)
                    alert();
                try {
                    DBConnection dbConnection = new DBConnection();
                    PreparedStatement stm = dbConnection.getConnection().prepareStatement("INSERT INTO dbo.Comment VALUES(?,?,?);");
                    stm.setString(1,comment.getText().toString());
                    stm.setInt(2,Leisure.getLeisureId());
                    stm.setString(3,User.getUserID());
                    stm.executeUpdate();
                    comment_adapter.add(User.getUserID() + "\n" + comment.getText().toString());
                    comment_adapter.notifyDataSetChanged();
                    dbConnection.close();
                }catch(SQLException e){}
                catch (ClassNotFoundException c){}
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommentActivity.this, SelectedLeisure.class));
            }
        });
    }

    private void init() {
        commentArrayList = new ArrayList<>();
        try {
            DBConnection dbConnection = new DBConnection();
            String query = "Select * from dbo.Comment where leisureID = '" + Leisure.getLeisureId() + "'";
            ResultSet rs = dbConnection.getStatement().executeQuery(query);
            while(rs.next()){
                commentArrayList.add(rs.getString("userID") + "\n" +rs.getString("comments"));
            }
            comment_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, commentArrayList);
            dbConnection.close();
        }catch(SQLException e){}
        catch (ClassNotFoundException c){}
    }
    private void alert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("You have to login to comment");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Login",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(CommentActivity.this,LoginActivity.class));
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
