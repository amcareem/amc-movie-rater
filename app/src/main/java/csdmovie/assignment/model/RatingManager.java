package csdmovie.assignment.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import csdmovie.assignment.model.Major;
import csdmovie.assignment.model.Movie;
import csdmovie.assignment.model.User;
import csdmovie.assignment.database.DBQuery;

public class RatingManager {

    private DBQuery dbq = new DBQuery();
    private static List<Rating> ratings = new ArrayList<>();
    UserManager um = UserManagerSingleton.getUserManager();
    MovieManager mm = new MovieManager();

    public void initializeRating(int userId, String movieId, float rating) {
        Rating newRating = new Rating(userId, movieId, rating);
        ratings.add(newRating);
    }

    /**
     * Add a rating to the database (Or update user's rating if it's already there)
     * @param movieId the OMDb id of a movie
     * @param userId the user id of the user rating the movie
     * @param rating the rating to add to the database for the movie
     */
    public void addRating(int userId, String movieId, float rating) {

        boolean isNewRating = true;
        boolean isNewMovie = true;
        Rating oldRating = null;

        //Looks through the old ratings to determine if the input rating is a new rating
        for (int i = 0; i < ratings.size(); i++) {
            oldRating = ratings.get(i);
            Log.d("LOOKHERE", oldRating.getMovieId());

            Log.d("LOOKHERE", movieId);
            if (oldRating.getMovieId().equals(movieId)) {
                isNewMovie = false;
            }
            if (oldRating.getMovieId().equals(movieId) && oldRating.getUserId() == userId) {
                isNewRating = false;
                break;
            }
        }

        if (isNewRating) {
            Rating newRating = new Rating(userId, movieId, rating);
            ratings.add(newRating);
            dbq.addRating(userId, movieId, rating);
        } else {
            oldRating.setRating(rating);
            dbq.editRating(userId, movieId, rating);
        }

        if (isNewMovie) {
            //data[0] = movieID data[1] = movieTitle data[2] = genre string
            List<String> data = mm.getMovieInfoByID(movieId);
            dbq.addMovie(data.get(0), data.get(1), data.get(2));
        }

    }


    /**
     * Get the average rating of a movie, regardless of major
     * @param movieId the OMDb id of a movie
     * @return the average rating
     */
    public float getAvgRating(String movieId) {
        return getAvgRating(movieId, null);
    }

    /**
     * Get the average rating, filtered by major
     * @param movieId The IMDB ID of the movie
     * @param major   The major to filter with
     * @return        The average rating of the movie, counting only those users of the given major
     */
    public float getAvgRating(String movieId, Major major) {

        Rating rating;
        float ratingSum = 0;
        int totalRatings = 0;
        float avg;
        int userId;
        User user;

        if (major == null) {
            for (int i = 0; i < ratings.size(); i++) {
                rating = ratings.get(i);
                if (rating.getMovieId().equals(movieId)) {
                    ratingSum += rating.getRating();
                    totalRatings++;
                }
            }
            if (totalRatings == 0) {
                return 0;
            }
            avg = ratingSum/totalRatings;
            return avg;
        } else {
            for (int i = 0; i < ratings.size(); i++) {
                rating = ratings.get(i);
                userId = rating.getUserId();
                user = um.findUserByID(userId + "");
                if (user.getMajor() == major && rating.getMovieId().equals(movieId)) {
                    ratingSum += rating.getRating();
                    totalRatings++;
                }
            }
            if (totalRatings == 0) {
                return 0;
            }
            avg = ratingSum/totalRatings;
            return avg;
        }
    }



    /**
     * Gets an ordered list of recommendations, according to highest rating from a specific major
     *
     * @param major The major to filter by
     *
     * @return A list of the top movie titles for the given major
     * output[0] = array of movie titles
     * output[1] = array of movie IDs
     */
    public String[][] getRecommendationsByMajor(Major major) {

        String majorStr = null;
        List<String> movieIds;
        List<List<String>> movieList;
        String[][] output = null;

        if (major != null) {
            majorStr = major.toString();
        }
        movieList = dbq.getMovieRecsByMajor(majorStr);
        output = new String[2][movieList.size()];
        for (int i = 0; i < movieList.size(); i++) {
            output[0][i] = movieList.get(i).get(0);
            output[1][i] = movieList.get(i).get(1);
        }

        return output;
    }

    /**
     * Gets overall recommendations, ordered in descending order. Not major specific
     * @return a 2D string array output[0] = movie titles
     * output[1] = movie IDs
     */
    public String[][] getOverallRecommendations() {
        return getRecommendationsByMajor(null);
    }

}
