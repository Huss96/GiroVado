package com.example.hussnain.girovado;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.darsh.multipleimageselect.models.Image;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SaveActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.list) ListView list;
    @BindView(R.id.material_design_android_floating_action_menu) FloatingActionMenu menu;
    @BindView(R.id.material_design_floating_action_menu_item1) FloatingActionButton save;
    @BindView(R.id.material_design_floating_action_menu_item2) FloatingActionButton edit;
    @BindView(R.id.material_design_floating_action_menu_item3) FloatingActionButton cancel;

    ArrayAdapter<String> transport_adapter;
    ArrayList<String> transportArrayList;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private FirebaseAuth auth;
    private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;
    private ArrayList<Uri> selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ButterKnife.bind(this);
        mProgressDialog = new ProgressDialog(SaveActivity.this);
        mProgressBar = findViewById(R.id.progress_bar);
        init();
        list.setAdapter(transport_adapter);
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        list,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    transportArrayList.remove(position);
                                    Leisure.getTransports().remove(position);
                                    transport_adapter.notifyDataSetChanged();

                                }


                            }
                        });
        list.setOnTouchListener(touchListener);
        save.setOnClickListener(this);
        edit.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == save) {
            saveLeisureDB();

            startActivity(new Intent(SaveActivity.this, MainActivity.class));
        }
        if (v == edit) {
            startActivity(new Intent(SaveActivity.this, TransportActivity.class));
        }
        if (v == cancel) {
            startActivity(new Intent(SaveActivity.this, MainActivity.class));
        }
    }

    private void init() {
        transportArrayList = new ArrayList<>();
        for (int i = 0; i < Leisure.getTransports().size(); i++)
            transportArrayList.add(Leisure.getTransports().get(i).getType() + "   " + Leisure.getTransports().get(i).getCompany());
        transport_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, transportArrayList);
    }

    private void saveLeisureDB() {
        try {

            DBConnection dbConnection = new DBConnection();
            String query;
            PreparedStatement stm = dbConnection.getConnection().prepareStatement("INSERT INTO dbo.Leisure VALUES(?,?,?,?,?,?,?,?,?,?);");
            stm.setInt(1,Leisure.getLeisureId());
            stm.setString(2,Leisure.getLeisureAddress());
            stm.setString(3,Leisure.getLeisureCity());
            stm.setString(4,Leisure.getLeisureDescription());
            stm.setInt(5,Leisure.getPlace());
            stm.setString(6,User.getUserID());
            stm.setString(7,Leisure.getLeisureName());
            stm.setString(8,Leisure.getDate());
            stm.setString(9,Leisure.getTime());
            stm.setString(10,Leisure.getTimeToDedicate());
            stm.executeUpdate();

            for(int i = 0; i<Leisure.getLeisureType().size();i++){
                query = "INSERT INTO dbo.LeisureType VALUES(" + Leisure.getLeisureId() + ",'" + Leisure.getLeisureType().get(i) +"');";
                dbConnection.getStatement().executeUpdate(query);
            }
            if (Leisure.getTransports().size() > 0) {
                for (int i = 0; i < Leisure.getTransports().size(); i++) {
                    stm = dbConnection.getConnection().prepareStatement("INSERT INTO dbo.Transport VALUES(?,?,?,?);");
                    stm.setString(1,Leisure.getTransports().get(i).getType());
                    stm.setString(2,Leisure.getTransports().get(i).getCompany());
                    stm.setString(3,Leisure.getTransports().get(i).getLink());
                    stm.setInt(4,Leisure.getLeisureId());
                    stm.executeUpdate();
                 }
            }

            query = "Select point from dbo.User_app where userID = '" + User.getUserID() + "'";
            ResultSet rs = dbConnection.getStatement().executeQuery(query);
            int cont = 0;
            if (rs.next())
                cont = rs.getInt("point");
            if(Leisure.getTransports().size() > 0)
                cont = cont + 2;
            if(GalleryAdapter.getmArrayUri().size() > 0)
                cont = cont + 3;
            cont = cont + 10;
            query = "update dbo.User_app set point = " + cont + " where userID = '" + User.getUserID() + "'";
            dbConnection.getStatement().executeUpdate(query);
            dbConnection.close();

        } catch (SQLException e) {

            e.printStackTrace();

        } catch (ClassNotFoundException c) {

        }
        insertImage();

    }

    public void insertImage() {

        Log.d("InsertImage", "onClick: Uploading Image.");
        mProgressDialog.setMessage("Uploading Image...");
        mProgressDialog.show();
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        auth.signInAnonymously();
        mStorageRef = FirebaseStorage.getInstance().getReference("image");

        String path;
        File file;
        String nameImage;
        selectedImage = new ArrayList<>();
        for (int i = 0; i < GalleryAdapter.getmArrayUri().size(); i++) {
            path = GalleryAdapter.getmArrayUri().get(i).getPath();
            file = new File(path);
            nameImage = file.getName();
            selectedImage.add(Uri.fromFile(file));
            StorageReference fileReference = mStorageRef.child(nameImage);

            mUploadTask = fileReference.putFile(selectedImage.get(i))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(SaveActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String url = task.getResult().toString();
                                    DBConnection dbConnection1 = null;
                                    String query;
                                    try {
                                        dbConnection1 = new DBConnection();
                                        query = "Insert into dbo.Image VALUES ('" + url + "'," + Leisure.getLeisureId() + ")";
                                        dbConnection1.getStatement().executeUpdate(query);
                                        dbConnection1.close();
                                    } catch (SQLException s) {
                                        s.printStackTrace();
                                    } catch (ClassNotFoundException c) {
                                        c.printStackTrace();
                                    }

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SaveActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });

        }
    }
}