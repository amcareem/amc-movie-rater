package csdmovie.assignment;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import findteamname.buzzmovieselector.controller.*;
import findteamname.buzzmovieselector.model.*;
import findteamname.buzzmovieselector.database.DBQuery;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


public class ApplicationTest {
    private final int TIMEOUT = 1000;
    private DBQuery dbQuery;
    private int id;
    private RatingManager ratingManager;
    private MovieManager movieManager;
    private Movie testMovie;
    private String movieId;
    @Before
    public void setup() {
        movieId = "tt2407380";
        dbQuery = new DBQuery();
        id = dbQuery.addUser("JUnit", "test", "CS", false);
        UserManagerSingleton.getUserManager().addUser(new User(id, "JUnit", "test", Major.CS, false));
        movieManager = new MovieManager();
        try {
            testMovie = movieManager.getMovieObject(new JSONObject("{\"Title\":\"Test\",\"Year\":\"2013\",\"Rated\":\"N/A\",\"Released\":\"04 Apr 2014\",\"Runtime\":\"89 min\",\"Genre\":\"Drama\",\"Director\":\"Chris Mason Johnson\",\"Writer\":\"Chris Mason Johnson (screenplay)\",\"Actors\":\"Scott Marlowe, Matthew Risch, Evan Boomer, Kevin Clarke\",\"Plot\":\"In 1985, a gay dance understudy hopes for his on-stage chance while fearing the growing AIDS epidemic.\",\"Language\":\"English, Portuguese, French\",\"Country\":\"USA\",\"Awards\":\"3 wins & 2 nominations.\",\"Poster\":\"http://ia.media-imdb.com/images/M/MV5BMTQwMDU5NDkxNF5BMl5BanBnXkFtZTcwMjk5OTk4OQ@@._V1_SX300.jpg\",\"Metascore\":\"70\",\"imdbRating\":\"6.5\",\"imdbVotes\":\"980\",\"imdbID\":\"tt2407380\",\"Type\":\"movie\",\"Response\":\"True\"}"));
        } catch (JSONException j) {
            testMovie = new Movie(movieId, "Test", "Drama");
        }
        movieManager.addMovie(testMovie);
        ratingManager = new RatingManager();
        ratingManager.addRating(id, movieId, 3f);    }

    @Test
    public void addUserTest() {
        assertEquals(dbQuery.addUser("JUnit", "test", "CS", false), id);
    }
    @Test
    public void loginAttemptTest() {
        assertTrue(UserManagerSingleton.getUserManager().handleLoginRequest("JUnit", "test"));
    }
    @Test
    public void loginAttemptWrongPsTest() {
        assertFalse(UserManagerSingleton.getUserManager().handleLoginRequest("JUnit","WrongPS"));
    }
    @Test
    public void getSearchURLTest() {
        assertEquals("http://www.omdbapi.com/?s=Batman", movieManager.getSearchURL("Batman", Movie.Type.ANY, 0, 0));
    }
    @Test
    public void getSearchElementsTest() {

    }

    @Test
    public void getMovieTitleByIDTest() {
        assertEquals("Test", movieManager.getMovieInfoByID("tt2407380").get(1));
    }
    @Test
    public void getAverageRatingTest() {
        assertEquals(new Double(3), new Double(ratingManager.getAvgRating(movieId)));
    }

}