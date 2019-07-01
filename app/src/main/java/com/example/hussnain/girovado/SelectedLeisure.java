package com.example.hussnain.girovado;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedLeisure extends AppCompatActivity implements View.OnClickListener {

    String image;
    private GestureDetector mGestureDetector;

    @BindView(R.id.flipper) ViewFlipper viewFlipper;
    @BindView(R.id.textTitle) TextView title;
    @BindView(R.id.textDescription) TextView description;
    @BindView(R.id.genInformation) TextView genInformation;
    @BindView(R.id.textAddress) TextView address;
    @BindView(R.id.textTime) TextView time;
    @BindView(R.id.textType) TextView textType;
    @BindView(R.id.textTransport) TextView textTranport;
    @BindView(R.id.material_design_android_floating_action_menu) FloatingActionMenu menu;
    @BindView(R.id.material_design_floating_action_menu_item1)
    com.github.clans.fab.FloatingActionButton comment;
    @BindView(R.id.material_design_floating_action_menu_item2)
    com.github.clans.fab.FloatingActionButton google;
    @BindView(R.id.material_design_floating_action_menu_item3)
    com.github.clans.fab.FloatingActionButton vote;

    @BindView(R.id.ratingAve) RatingBar ratingAve;

    float cont = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedleisure);
        ButterKnife.bind(this);
        loadImage();
        loadOtherThings();
        comment.setOnClickListener(this);
        google.setOnClickListener(this);
        vote.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == comment) {

            startActivity(new Intent(SelectedLeisure.this, CommentActivity.class));
        }
        if (v == google) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address.getText().toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
        if (v == vote) {
            if(User.getConnected()== false)
                alert();
            ShowDialog();
        }
    }
    public void loadOtherThings(){
        try{
            DBConnection dbConnection = new DBConnection();
            String query = null;
            ResultSet rs = null;

            query = "select * from dbo.Leisure where leisureID like '" + Leisure.getLeisureId() + "'";
            rs = dbConnection.getStatement().executeQuery(query);
            if(rs.next()) {
                title.setText(rs.getString("name"));
                description.setText(rs.getString("description")+ "\n");
                genInformation.setText("The time to dedicate is: " + rs.getString("timeToDedicate")+ "\n");
                address.setText(rs.getString("address") + ", " + rs.getString("city"));
                if(rs.getInt("placeActivity") == 0){
                    time.setVisibility(View.VISIBLE);
                    time.setText("The time and date of acticity is " + rs.getString("time") + ", " + rs.getString("date")+ "\n");
                }
                else
                    time.setVisibility(View.INVISIBLE);
            }
            query = "select type from dbo.LeisureType where leisureID like '" + Leisure.getLeisureId() + "'";
            rs = dbConnection.getStatement().executeQuery(query);
            String types = "The leisure type:";
            while(rs.next()){
                types = types + ", " + rs.getString("type");
            }
            types = types + "\n";
            textType.setText(types);
            query = "select * from dbo.Transport where leisureId like '" + Leisure.getLeisureId() + "'";
            rs = dbConnection.getStatement().executeQuery(query);
            types = "Tranport: \n";
            boolean transport = false;
            while(rs.next()){
                transport = true;
                types = types + rs.getString("type") + " " + rs.getString("companyName") + " " + rs.getString("link") + "\n";
            }
            if(transport) {
                textTranport.setVisibility(View.VISIBLE);
                textTranport.setText(types + "\n");
                textTranport.setMovementMethod(LinkMovementMethod.getInstance());
            }
            else
                textTranport.setVisibility(View.INVISIBLE);
            query = "select avg(stars) as average from dbo.Rating group by leisureID";
            rs = dbConnection.getStatement().executeQuery(query);
            if(rs.next())
                ratingAve.setRating(rs.getFloat("average"));
            else
                ratingAve.setRating(0);
            dbConnection.close();

        }catch(SQLException s){
            s.printStackTrace();
        }
        catch (ClassNotFoundException c){}

    }
    public void loadImage(){
        try {
            DBConnection dbConnection = new DBConnection();
            String query = null;
            ResultSet rs = null;
            int cont = 0;
            query = "select photo from dbo.Image where leisureId like '" + Leisure.getLeisureId() + "'";
            rs = dbConnection.getStatement().executeQuery(query);
            while(rs.next()) {
                cont = 1;
                ImageView imageView = new ImageView(this);
                image = rs.getString("photo");
                viewFlipper.addView(imageView);
                Picasso.get()
                        .load(image)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(imageView);
            }
            if(cont == 0){
                ImageView imageView = new ImageView(this);
                viewFlipper.addView(imageView);
                Picasso.get()
                        .load(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(imageView);
            }
            dbConnection.close();

        }catch(SQLException s){
            s.printStackTrace();
        }
        catch (ClassNotFoundException c){}
        viewFlipper.setInAnimation(this, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(this, android.R.anim.fade_out);
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(this, customGestureDetector);

    }

    private void alert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("You have to login to rate");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Login",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(SelectedLeisure.this,LoginActivity.class));
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
    private void ShowDialog()
    {

        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final RatingBar rating = new RatingBar(this);
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("Vote!! ");
        rating.setLayoutParams(lParams);
        rating.setNumStars(5);
        linearLayout.addView(rating);
        popDialog.setView(linearLayout);

        try{
            DBConnection dbConnection = new DBConnection();
            String query = "Select * from dbo.Rating where userID = '" + User.getUserID() + "' AND leisureID  = " + Leisure.getLeisureId() + ";";
            ResultSet rs = dbConnection.getStatement().executeQuery(query);
            if (rs.next())
                cont = rs.getFloat("stars");
            else
                cont = 0;
            rating.setRating(cont);
        }catch (SQLException e) { e.printStackTrace(); }
        catch (ClassNotFoundException c) { }

        popDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try{
                    DBConnection dbConnection = new DBConnection();
                    String query;
                    if(cont == 0)
                        query = "INSERT INTO dbo.Rating VALUES(" + rating.getRating() + "," + Leisure.getLeisureId() + ", '" + User.getUserID() + "')";
                    else
                        query = "UPDATE dbo.Rating SET stars = " + rating.getRating() + " where leisureID = " + Leisure.getLeisureId() + " and userID = '" + User.getUserID()+"'";
                    dbConnection.getStatement().executeUpdate(query);
                    dbConnection.close();
                }catch (SQLException e) { e.printStackTrace(); }
                catch (ClassNotFoundException c) { }

                dialog.dismiss();
            }});
        popDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        popDialog.create();

        popDialog.show();

    }
    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                viewFlipper.showNext();
            }

            // Swipe right (previous)
            if (e1.getX() < e2.getX()) {
                viewFlipper.showPrevious();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

}
