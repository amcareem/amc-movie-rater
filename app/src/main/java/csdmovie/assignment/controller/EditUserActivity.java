package csdmovie.assignment.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import csdmovie.assignment.R;
import csdmovie.assignment.database.DBQuery;
import csdmovie.assignment.model.Admin;
import csdmovie.assignment.model.Movie;
import csdmovie.assignment.model.User;
import csdmovie.assignment.model.UserManager;
import csdmovie.assignment.model.UserManagerSingleton;

public class EditUserActivity extends AppCompatActivity {

    ListView ratingsList;
    TextView ban;
    Button banButton;
    TextView username;
    TextView major;
    private static DBQuery db = new DBQuery();

    boolean banned;
    User user;
    UserManager userManager;
    String userName;
    HashMap<Movie, Float> ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        userManager = UserManagerSingleton.getUserManager();
        ratingsList = (ListView) findViewById(R.id.listView3);
        ban = (TextView) findViewById(R.id.isBanned);
        username = (TextView) findViewById(R.id.username);
        major = (TextView) findViewById(R.id.major);
        banButton = (Button) findViewById(R.id.banButton);
/**
 * Author:    Ahamed Careem
 **/

        // store clicked userName on intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("userId");
        }

        user = userManager.findUserByID(userName);
        banned = user.isBanned();
        ratings = user.getRatings();

        if (user instanceof Admin) {
            username.setText("Admin: " + user.getUserName());
            major.setText("No major");
        } else {
            username.setText("User: " + user.getUserName());
            major.setText("Major: " + user.getMajor() + "");
        }



        if (banned) {
            ban.setText("Banned: Yes");
            banButton.setText("Un-Ban");
        } else {
            ban.setText("Banned: No");
            banButton.setText("Ban");
        }

        setRatingsView();
    }

    /**
     * Locally bans user and pushes changes to the database
     * @param v View passed in from xml
     */
    public void banUser(View v) {
        if (user instanceof Admin) {
            CharSequence text = "You CANNOT ban an Admin";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
          return ;
        }
        if (banned) {
            ban.setText("Banned: No");
            banButton.setText("Ban");
            banned = false;
            //now push to database & user class
            db.editUser(user.getUserID() + "", "Banned", "0");
            ((Admin) UserManagerSingleton.getUser()).unBan(user);
        } else {
            ban.setText("Banned: Yes");
            banButton.setText("Un-Ban");
            banned = true;
            //also push changes to database
            db.editUser(user.getUserID() + "", "Banned", "1");
            ((Admin) UserManagerSingleton.getUser()).ban(user);
        }
    }

    /**
     * Helper method to populate the listview with all the ratings
     */
    public void setRatingsView() {
        CharSequence text = "have yet to populate listview";
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

        Set<Movie> movies = ratings.keySet();
        ArrayList<Float> movieRatings = new ArrayList<>(ratings.values());
    }


}
