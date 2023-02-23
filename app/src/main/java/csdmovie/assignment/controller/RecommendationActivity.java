/**
 * Author:    Ahamed Careem
 **/
package csdmovie.assignment.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import csdmovie.assignment.R;
import csdmovie.assignment.model.RatingManager;
import csdmovie.assignment.model.Movie;
import csdmovie.assignment.model.MovieManager;
import csdmovie.assignment.model.User;
import csdmovie.assignment.model.UserManagerSingleton;

public class RecommendationActivity extends AppCompatActivity {

    private ListView movieList;
    private String[][] recommendations;
    private RatingManager db = new RatingManager();
    private User currentUser = UserManagerSingleton.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        movieList = (ListView) findViewById(R.id.recommendationsList);
        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieManager.setMovieTitle(recommendations[1][position]);
                displayMovie();
            }
        });
    }

    /**
     * Displays the top movies based on the current user's major.
     *
     * @param v The My Major button
     */
    public void getMoviesByMajor(View v) {
        recommendations = db.getRecommendationsByMajor(currentUser.getMajor());
        setMovieList();
    }

    /**
     * Displays the top movies overall.
     *
     * @param v The Overall Movies button
     */
    public void getOverallMovies(View v) {
        recommendations = db.getOverallRecommendations();
        setMovieList();
    }

    /**
     * Displays the movie the user selected from the list of recommendations.
     */
    private void displayMovie() {
        startActivity(new Intent(this, MovieDisplayActivity.class));
    }

    private void setMovieList() {
        movieList.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                recommendations[0]
        ));
    }
}
