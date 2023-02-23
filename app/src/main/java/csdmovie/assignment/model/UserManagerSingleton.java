package csdmovie.assignment.model;


public class UserManagerSingleton extends UserManager {

    private static User user;
    private static UserManager userManager;

    /**
     * Private constructor to prevent instantiation
     */
    private UserManagerSingleton() {

    }

    /**
     * Sets the current user of the app.
     *
     * @param userSet The current user
     */
    public static void setUser(User userSet) {
        user = userSet;
    }

    /**
     * @return The current user of the app
     */
    public static User getUser() {
        return user;
    }

    /**
     * @return The current UserManager
     */
    public static UserManager getUserManager() {
        if (userManager == null) {
            userManager = new UserManager();
        }

        return userManager;
    }
}
