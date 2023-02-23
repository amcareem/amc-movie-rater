package csdmovie.assignment.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;

import csdmovie.assignment.R;
import csdmovie.assignment.model.MovieManager;
import csdmovie.assignment.model.Movie;
import csdmovie.assignment.model.APIComm;

public class MovieSearchActivity extends AppCompatActivity {
    private MovieManager movieManager;
    String searchedTitle;
    ListView movieList;
    Movie.Type type;
    Context c;
    Spinner typeSpinner;
    int year;
    JSONObject response;
    private int totalPages;
    private int currentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        totalPages = 0;
        currentPage = 0;
        c = this;
        typeSpinner = (Spinner) findViewById(R.id.searchTypeSpinner);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayList<Movie.Type> types = new ArrayList<>();
        types.add(null);
        for (Movie.Type a : Movie.Type.values()) {
            types.add(a);
        }
        ArrayAdapter<Movie.Type> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Movie.Type.values());
        Spinner typeSpinner = (Spinner) findViewById(R.id.searchTypeSpinner);
        typeSpinner.setAdapter(adapter);
        final Spinner tSpinner = typeSpinner;
        Log.d("FIRST", "about to get instance.");
        APIComm.getInstance(this);

        movieManager = new MovieManager();
        movieList = (ListView) findViewById(R.id.listView);
        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = Movie.Type.values()[tSpinner.getSelectedItemPosition()];
                try {
                    year = Integer.parseInt(((EditText) findViewById(R.id.searchYear)).getText().toString());
                } catch (Exception e) {
                    year = -1;
                }
                MovieManager.setMovieTitle(movieManager.getSearchElements(response, "imdbID")[position]);
                displayMovie();
            }
        });

    }

    /**
     * Displays the movie activity.
     */
    public void displayMovie() {
        startActivity(new Intent(this, MovieDisplayActivity.class));
    }

    /**
     * Loads the search results based on the movie title passed in.
     *
     * @param v The search button that called the search function
     */
    public void onSearchButtonPressed(View v) {
        searchedTitle = ((EditText) findViewById(R.id.searchBar)).getText().toString();
        typeSpinner = (Spinner) findViewById(R.id.searchTypeSpinner);
        type = Movie.Type.values()[typeSpinner.getSelectedItemPosition()];

        try {
            year = Integer.parseInt(((EditText) findViewById(R.id.searchYear)).getText().toString());
        } catch (Exception e) {
            year = -1;
        }

        JsonObjectRequest request = getRequest();

        /*ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,movieManager.getSearchResults(searchedTitle, type, year, 1));
        movieList.setAdapter(adapter);*/

        APIComm.add(request);
    }

    private JsonObjectRequest getRequest() {
        return getRequest(1);
    }

    private JsonObjectRequest getRequest(final int page) {
        return new JsonObjectRequest(Request.Method.GET,
                movieManager.getSearchURL(searchedTitle, type, year, page), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resp) {
                handleResponse(resp, page);
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
    }

    /**
     *
     */
    private void handleResponse(JSONObject resp, int page) {
        //handle a valid response coming back.  Getting this string mainly for debug
        //printing first 500 chars of the response.  Only want to do this for debug
        Log.d("STATUS", resp.toString());
        if (resp.optString("Response").equals("False")) {
            CharSequence text = "No results found";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(c,
                    android.R.layout.simple_list_item_1, new String[0]);
            movieList.setAdapter(adapter);
            return;
        }
        response = resp;
        currentPage = page;
        totalPages = (Integer.parseInt(resp.optString("totalResults")) - 1) / 10 + 1;
        setButtonVisibility();
        Log.d("RESULTS:", "" + totalPages);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(c,
                android.R.layout.simple_list_item_1,movieManager.getSearchElements(resp, "Title"));
        movieList.setAdapter(adapter);
    }

    private void setButtonVisibility() {
        Button nextButton = (Button) findViewById(R.id.nextPageButton);
        Button previousButton = (Button) findViewById(R.id.previousPageButton);
        if (currentPage < totalPages) {
            nextButton.setVisibility(View.VISIBLE);
            Log.d("STATUS" , "VISIBLE");
        } else {
            nextButton.setVisibility(View.GONE);
            Log.d("STATUS" , "Invisible");
        }
        if (currentPage > 1) {
            previousButton.setVisibility(View.VISIBLE);
        } else {
            previousButton.setVisibility(View.GONE);
        }
    }

    public void onNextPageClicked(View v) {
        currentPage++;

        JsonObjectRequest request = getRequest(currentPage);

        /*ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,movieManager.getSearchResults(searchedTitle, type, year, 1));
        movieList.setAdapter(adapter);*/

        APIComm.add(request);
        setButtonVisibility();
    }

    public void onPreviousPageClicked(View v) {
        currentPage--;
        JsonObjectRequest request = getRequest(currentPage);

        APIComm.add(request);
        setButtonVisibility();
    }

    /**
     * Goes back to main menu
     *
     * @param v The back button
     */
    public void onBackButtonPressed(View v) {
        finish();
    }
}
