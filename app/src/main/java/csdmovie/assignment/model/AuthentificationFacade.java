package csdmovie.assignment.model;

/**
 * Represents authentification of a user trying to log in.
 */
public interface AuthentificationFacade {

    /**
     * Checks if the given combination of userID and password matches a user of the app.
     *
     * @param userID The ID of the user trying to log in
     * @param password The user's password
     *
     * @return True if the user has a valid ID and password, false otherwise
     */
    public boolean handleLoginRequest(String userID, String password);
}
