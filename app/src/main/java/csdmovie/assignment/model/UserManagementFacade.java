package csdmovie.assignment.model;

import java.util.ArrayList;


public interface UserManagementFacade {

    /**
     * Adds the given user to the list of users.
     *
     * @param user The new user to add
     */
    public void addUser(User user);

    /**
     * Removes the given user from the list of users.
     *
     * @param user The user to remove
     *
     * @return True if the user was removed, false otherwise
     */
    public boolean removeUser(User user);

    /**
     * Gets the user with the given user ID from the list of users.
     *
     * @param id The user ID to search for
     *
     * @return The user with the given ID, or null if the user does not exist
     */
    public User findUserByID(String id);
}
