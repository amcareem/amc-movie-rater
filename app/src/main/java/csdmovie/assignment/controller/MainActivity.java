package csdmovie.assignment.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import csdmovie.assignment.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    /**
     * Logs out the user and goes back to the opening screen.
     *
     * @param v The view that called this function (the LOGOUT button)
     */
    public void onLogoutButtonPressed(View v) {
        finish();
    }

    /**
     * Takes the user to the profile editing screen.
     *
     * @param v The view that called this method (the EDIT PROFILE button)
     */
    public void onEditProfileButtonPressed(View v) {
        startActivity(new Intent(this, EditProfileActivity.class));
    }

    /**
     * Takes user to movie searching activity
     * @param v The search button
     */
    public void onSearchMovieButtonPressed(View v) {
        startActivity(new Intent(this, MovieSearchActivity.class));
    }

    /**
     * Takes user to the movie reccomendation screen.
     *
     * @param v The recommend movies button
     */
    public void onRecommendButtonPressed(View v) {
        startActivity(new Intent(this, RecommendationActivity.class));
    }
}
