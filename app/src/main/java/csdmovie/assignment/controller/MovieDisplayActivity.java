package csdmovie.assignment.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import csdmovie.assignment.R;
import csdmovie.assignment.model.RatingManager;
import csdmovie.assignment.model.APIComm;
import csdmovie.assignment.model.Movie;
import csdmovie.assignment.model.MovieInformationFacade;
import csdmovie.assignment.model.MovieManager;
import csdmovie.assignment.model.User;
import csdmovie.assignment.model.UserManagerSingleton;

public class MovieDisplayActivity extends AppCompatActivity {
    Movie movie;
    RatingBar userRatingBar;
    RatingManager ratingDataBase = new RatingManager();
    User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userRatingBar = (RatingBar) findViewById(R.id.userRatingBar);

        //MovieInformationFacade movieManager = new MovieManager();


        final MovieInformationFacade movieManager = new MovieManager();
        //information = movieManager.displayInformation(MovieManager.getMovieTitle());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                movieManager.getDisplayURL(MovieManager.getMovieTitle()), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                //handle a valid response coming back.  Getting this string mainly for debug
                //printing first 500 chars of the response. For debug
                Log.d("STATUS", resp.toString());
                if (resp.optString("Response").equals("False")) {
                    CharSequence text = "No results found";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    return;
                }
                /**
                 * Author:    Ahamed Careem
                 **/
                //Get and display all of movie's attributes
                movie = movieManager.getMovieObject(resp);
                TextView title = (TextView) findViewById(R.id.MovieTitle);
                TextView year = (TextView) findViewById(R.id.MovieYear);
                TextView description = (TextView) findViewById(R.id.MovieDescription);
                title.setText(movie.getTitle());
                year.setText("" + movie.getYear());
                description.setText(movie.getDescription());

                currentUser = UserManagerSingleton.getUser();
                //userRatingBar.setRating(rm.getAvgRating(movie.getID()));
                userRatingBar.setRating(currentUser.getMovieRating(movie));


                RatingBar avgRatingBar = (RatingBar) findViewById(R.id.avgRatingBar);
                avgRatingBar.setRating(ratingDataBase.getAvgRating(movie.getID()));
                //add rating to user's movie ratings
                userRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        float currentUserRating = userRatingBar.getRating();
                        currentUser.rateMovie(movie, currentUserRating);
                        (new MovieManager()).addMovie(movie);
                        ratingDataBase.addRating(currentUser.getUserID(), movie.getID(), currentUserRating);
                    }
                });


                //send movie and rating to the database

                //add movie to movie manager
//                MovieManager.setRating(movie, currentUserRating);


//                avgRatingBar.setRating(MovieManager.getRating(movie));
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String response = "JSon Request Failed!!";
                //show error on phone
                TextView view = (TextView) findViewById(R.id.textView2);
                view.setText(response);
            }
        });

        APIComm.getInstance().add(request);

//        User currentUser = UserManagerSingleton.getUser();
//        CharSequence text = "" + currentUser.getMovieRating(movie);
//        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
//        RatingBar rb = (RatingBar) findViewById(R.id.movieDisplayRatingBar);
//        rb.setRating(currentUser.getMovieRating(movie));

    }

    /**
     * Returns user to search activity
     * @param v View passed in from user click
     */
    public void onBackToSearchClicked(View v) {
        finish();
    }

}
