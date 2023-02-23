package csdmovie.assignment.model;

import org.json.JSONObject;
import java.util.Map;

public interface MovieInformationFacade {
    /**
     * Gets the url we need to search OMDB given a title
     * @param title
     * @return
     */
    public String getDisplayURL(String title);
    /**
     * Lists information about movie, to be used in an activity where specific movie has been
     * selected. If we want to add more things, add an enumerated element to MovieElement enum
     * @param json
     * @return title, description, and year of movie
     */
    public Movie getMovieObject(JSONObject json);
}
