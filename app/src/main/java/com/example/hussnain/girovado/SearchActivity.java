package com.example.hussnain.girovado;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.transportSearch) Spinner transport;
    @BindView(R.id.typeSearch) Spinner type;
    @BindView(R.id.timeSearch) Spinner time;
    @BindView(R.id.citySearch) EditText city;
    @BindView(R.id.searchButton) Button search;
    @BindView(R.id.cancelSearch) Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        //create the adpter for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.typeSearch,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.timeSearch,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(adapter2);
        ArrayAdapter<CharSequence> adapter3= ArrayAdapter.createFromResource(this, R.array.transportSearch, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transport.setAdapter(adapter3);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
                startActivity(new Intent(SearchActivity.this,FoundActivity.class));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this,MainActivity.class));
            }
        });
    }
    public void load(){
        try {
            final List<FoundLeisure> leisures = new ArrayList<>();
            DBConnection dbConnection = new DBConnection();

            String query = null;
            ResultSet rs = null;
            String city, typeS, timeS, transportS;
            String image = null, name;

            if(type.getSelectedItem().toString().equals("None"))
                typeS = "%";
            else
                typeS = type.getSelectedItem().toString();
            if(time.getSelectedItem().toString().equals("None"))
                timeS = "%";
            else
                timeS = time.getSelectedItem().toString();
            if(transport.getSelectedItem().toString().equals("None"))
                transportS = "%";
            else
                transportS = transport.getSelectedItem().toString();

            query = "Select dbo.Leisure.leisureId " +
                    "from dbo.Leisure join dbo.Transport on (dbo.Leisure.leisureId) = (dbo.Transport.leisureId) join dbo.Image on (dbo.Leisure.leisureId) = (dbo.Image.leisureId) join dbo.LeisureType on (dbo.LeisureType.leisureId) = (dbo.Leisure.leisureId)" +
                    "where dbo.LeisureType.type like '" +typeS+"' and dbo.Transport.type like '"+transportS+"' and timeToDedicate like '"+timeS+"' " +
                    "group by dbo.Leisure.leisureId";
            ArrayList<Integer> leisureIdList = new ArrayList<>();

            rs = dbConnection.getStatement().executeQuery(query);

            while (rs.next()) {
                leisureIdList.add(rs.getInt("leisureId"));

            }
            for(int i = 0; i<leisureIdList.size(); i++) {
                query = "Select city, name, photo " +
                        "from dbo.Leisure join dbo.Image on (dbo.Leisure.leisureId) = (dbo.Image.leisureId) " +
                        "where dbo.Leisure.leisureId like " + leisureIdList.get(i);
                rs = dbConnection.getStatement().executeQuery(query);
                if (rs.next()) {
                    name = rs.getString("name");
                    city = rs.getString("city");
                    image = rs.getString("photo");
                    leisures.add(new FoundLeisure(leisureIdList.get(i), name + ",  " + city, image));

                }
            }
            rs.close();
            dbConnection.close();
            ListFoundLeisure listFoundLeisure = new ListFoundLeisure(leisures);
        }
        catch(SQLException s){
            s.printStackTrace();
        }
        catch (ClassNotFoundException c){}

    }

}
