package csdmovie.assignment.model;

import org.json.JSONObject;


public interface MovieSearcherFacade {
    /**
     * Returns a list of titles from search result from open movie database at a specific page
     * @param search
     * @return a list of titles of movies related to search term
     */
    public String getSearchURL(String search, Movie.Type category, int year, int page);

    /**
     * Returns an array of titles from the json object passed in
     * @param json the json object to parse
     * @param tag the name of the element that we need to search for
     * @return the string array from the json object
     */
    public String[] getSearchElements(JSONObject json, String tag);
}
