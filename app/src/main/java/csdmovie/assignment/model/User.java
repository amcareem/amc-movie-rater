package csdmovie.assignment.model;

import java.util.HashMap;
import csdmovie.assignment.database.DBQuery;

public class User {

    private int userID;
    private String userName;
    private String password;
    private String bio;
    private Major major;
    private HashMap<Movie, Float> ratings;
    private boolean isBanned;

    public User(int userID, String userName, String password, Major major, boolean isBanned) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.bio = "";
        this.major = major;
        this.isBanned = isBanned;
        this.ratings = new HashMap<>();
    }

    public int getUserID() {
        return userID;
    }

    /**
     * @return The user's ID
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user's ID to the new ID if it is not empty.
     *
     * @param newUserName The new ID for the user
     */
    public void setUserName(String newUserName) {
        if (!newUserName.isEmpty()) {
            userName = newUserName;
        }
    }

    /**
     * @return The user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets a new password
     * @param newPass the new password
     */
    public void setPassword(String newPass) {
        password = newPass;
    }

    /**
     * @return The user's bio
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets the user's bio to the new bio.
     *
     * @param bio The new bio for the user
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * @return The user's major
     */
    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    /**
     *
     * @return true if user has been banned by admin
     */
    public boolean isBanned() {
        return isBanned;
    }

    /**
     * Allows admin to ban users
     * @param banned true to ban, false to unban
     */
    protected void setBanned(boolean banned) {
        isBanned = banned;
    }

    /**
     * Checks if the given password matches the user's actual password.
     *
     * @param password The password given at log in
     *
     * @return True if the password matches, false otherwise
     */
    public boolean validate(String password) {
        String DBPass;
        DBQuery query = new DBQuery();
        DBPass = query.getPassByUsername(this.userName);
        return DBPass.equals(password);
    }

    /**
     * Adds a new rating to the hashmap
     * @param movie the movie to be rated
     * @param rating the rating
     */
    public void rateMovie(Movie movie, Float rating) {
        ratings.put(movie, rating);
    }

    /**
     * Returns the user's rating for the given movie, or 0 if they have not rated the movie.
     *
     * @param movie The movie to request a rating for
     *
     * @return The user's rating of the movie, or 0 if they have not rated it
     */
    public Float getMovieRating(Movie movie) {
        Float rating = ratings.get(movie);
        return rating != null ? rating : 0f;
    }

    /**
     * Basic getter function
     * @return the hashmap of ratings
     */
    public HashMap<Movie, Float> getRatings() {
       return ratings;
    }

    @Override
    public String toString() {
        return userName + (this instanceof Admin ? " (admin)": "") + (isBanned ? " - Banned" : "");
    }
}
