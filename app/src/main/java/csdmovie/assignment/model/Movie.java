package csdmovie.assignment.model;
/**
 * Author:    Ahamed Careem
 **/
public class Movie {
    String Year;
    String Description;
    String Title;
    String ID;
    String Genre;

    public Movie(String ID) {
        this.ID = ID;
    }

    public Movie(String ID, String Title, String Genre) {
        this.ID = ID;
        this.Title = Title;
        this.Genre = Genre;
    }

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getDescription() {
        return Description;
    }

    public String getID() {
        return ID;
    }

    public String getGenre() {
        return Genre;
    }

    /**
     * An enum for type based on type parameter of IMDB API
     */
    public enum Type {
        ANY, MOVIE, SERIES
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else
            return other instanceof Movie && this.ID.equals(((Movie) other).ID);
    }

    @Override
    public int hashCode() {
        return ID.hashCode()
                + Year.hashCode()
                + Description.hashCode()
                + Title.hashCode();
    }

    @Override
    public String toString() {
        return ID;
    }
}
