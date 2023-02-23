package csdmovie.assignment.model;

public class Rating {
    public Rating(int userId, String movieId, float rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    private int userId;
    private String movieId;
    private float rating;
}
