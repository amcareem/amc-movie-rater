package csdmovie.assignment.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import csdmovie.assignment.model.Major;


public class DBQuery {

    @SuppressLint("NewApi")
    //added static. we'll see if that works -> attaboy
    public static Connection CONN() {
        String ip = "s12.winhost.com";
        String classs = "net.sourceforge.jtds.jdbc.Driver";
        String db = "DB_100424_appdb";
        String un = "DB_100424_appdb_user";
        String password = "csdmovie";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);

        } catch (SQLException se) {
            Log.e("ERRORDB", se.getMessage());

        } catch (ClassNotFoundException e) {
            Log.e("ERRORDB", e.getMessage());

        } catch (Exception e) {
            Log.e("ERRORDB", e.getMessage());

        }
        return conn;

    }

    private static Connection con = CONN();
    private static Statement stmt;

    /**
     * Constructor creates a statement using the connection for execution in the class
     */
    public DBQuery() {
        try {
            stmt = con.createStatement();
            Log.d("DB Connection", "Connection achieved");
        } catch (Exception e) {
            Log.d("DBQuery Error: ", e.getMessage());
        }
    }
    /**
     * Author:    Ahamed Careem
     **/
    /**
     * Executes a query on the server. Returns the result of the query entered
     *
     * @param query the command the sql server will execute
     * @return returns the table from the query if successful, returns null if not
     */
    private ResultSet executeQuery(String query) {

        ResultSet rs;
        Log.d("Execute Query: ", "Query string: " + query);
        try {
            rs = stmt.executeQuery(query);
            rs.next();
            return rs;
        } catch (Exception e) {
            Log.d("Execute Query Error: ", e.getMessage());
        }
        //If the ResultSet wasn't properly received, return null
        return null;
    }

    /**
     * Gets the column from the ResultSet from the server response. Searches for the name input
     * from the user. If the column exists in the query, it will return a string containing
     * the output value
     *
     * @param rs the result set from the server query
     * @param colName the column that data will be taken from
     * @return returns a string value of the contents of the current row at specified column
     * or returns null if there is an error that occurs
     */
    private String getColumnValue(ResultSet rs, String colName) {
        try {
            return rs.getString("Pass");
        } catch(Exception e) {
            Log.d("getColumnValue: ", "colName = " + colName + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Takes a username and looks for the matching password in the database
     *
     * @param username the username being searched
     * @return the password associated with the username if the username exists
     * If the username doesn't exist in the database, returns null
     */
    public String getPassByUsername(String username) {
        username = sanitizeInput(username);
        ResultSet rs = executeQuery("SELECT [Pass] FROM [Users] WHERE [Username] = '" + username + "'");
        Log.d("getPassByUsername", rs.toString());
        String pass = getColumnValue(rs, "Pass");
        return pass;
    }

    /**
     * Returns the database table for all users (including admins)
     *
     * @return a 2D list. The outer list contains rows and the
     * inner list is the strings of the column contents
     */
    public List<List<String>> getUsersTable() {
        ArrayList<List<String>> users = new ArrayList<>();
        String userID;
        String username;
        String pass;
        String major;
        String banned;
        String admin;
        int counter = 0;
        ResultSet rs = executeQuery("SELECT * FROM [Users]");
        if (rs == null) {
            return null;
        }
        try {
            do {

                users.add(new ArrayList<String>());

                userID = rs.getString("UserID");
                username = rs.getString("UserName");
                pass = rs.getString("Pass");
                major = rs.getString("Major");
                banned = rs.getString("Banned");
                admin = rs.getString("Admin");

                List<String> userRow = users.get(counter);
                userRow.add(userID);
                userRow.add(username);
                userRow.add(pass);
                userRow.add(major);
                userRow.add(banned);
                userRow.add(admin);

                counter++;

            } while (rs.next());
        } catch (java.sql.SQLException e) {
            Log.d("getUsersTable", e.getMessage());
        }
        Log.d("getUsersTable", users.toString());
        return users;
    }


    /**
     * Adds a new user to the database
     * @param username username
     * @param pass password
     * @param major major (allowed to be null for admins) please use the enum and convert to string
     * @param admin whether the user is an admin or not (true = admin)
     * @return returns the UserID generated by the database if there is an error in the database,
     * this method returns -1
     */
    public int addUser(String username, String pass, String major, boolean admin) {
        username = sanitizeInput(username);
        pass = sanitizeInput(pass);
        String adminStr;
        ResultSet rs;
        String userId = "-1";

        if (admin) {
            adminStr = "1";
        } else {
            adminStr = "0";
        }
        if (!admin && major == null) {
            throw new java.lang.IllegalArgumentException("No null major for non-admin users");
        }
        if (username == null || pass == null) {
            throw new java.lang.IllegalArgumentException("No null username or pass for new users");
        }
        executeQuery("INSERT INTO Users " +
                "(Username, Pass, Major, Banned, Admin)" +
                "Values ('" + username + "', '" + pass + "', '" + major + "', '" + "0', '" + adminStr + "');");
        rs = executeQuery("SELECT [UserID] FROM [Users] WHERE [UserName] = '" + username + "';");
        try {
            userId = rs.getString("UserID");
        } catch (java.sql.SQLException e) {
            Log.d("addUser", e.getMessage());
        }
        return Integer.parseInt(userId);
    }

    /**
     * Edits the column specified for a specific user and puts a new value in there
     * valid column names are UserName, Pass, Major, Banned, Admin
     * NEVER UPDATE UserID. This is managed by the SQL server
     * @param userId the user's user ID (the unique key for the rows)
     * @param columnName
     * @param newValue
     */
    public void editUser(String userId, String columnName, String newValue) {
        newValue = sanitizeInput(newValue);
        executeQuery("UPDATE [Users] SET " + columnName + " = '" + newValue + "' WHERE [UserID] = '" + userId + "'");
    }

    /**
     * Changes the given user's user name.
     *
     * @param userID      The user ID of the user to edit
     * @param newUserName The new user name
     */
    public void editUserName(int userID, String newUserName) {
        editUser(String.valueOf(userID), "UserName", newUserName);
    }

    /**
     * Changes the given user's password
     *
     * @param userID      The user ID of the user to edit
     * @param newPassword The new password
     */
    public void editPassword(int userID, String newPassword) {
        editUser(String.valueOf(userID), "Pass", newPassword);
    }

    /**
     * Changes the given user's major
     *
     * @param userID   The user ID of the user to edit
     * @param newMajor The new major
     */
    public void editMajor(int userID, Major newMajor) {
        editUser(String.valueOf(userID), "Major", newMajor.name());
    }

    /**
     * Sets whether or not the given user is banned.
     *
     * @param userID   The user ID of the user to edit
     * @param isBanned Whether or not the user is banned
     */
    public void setBanned(int userID, boolean isBanned) {
        editUser(String.valueOf(userID), "Banned", isBanned ? "1" : "0");
    }

    /**
     * Adds a rating to the database
     * @param userId the ID of the user who rated the movie
     * @param movieId the movie ID string from OMDb
     * @param rating the rating 0.5-5 of the movie
     */
    public void addRating(int userId, String movieId, double rating) {
        String id = userId + "";
        String ratingStr = rating + "";
        executeQuery("INSERT INTO Ratings " +
                "(UserID, MovieID, Rating)" +
                " Values ('" + id + "', '" + movieId + "', '" + rating + "');");
    }


    /**
     * Edits an existing rating in the database
     * @param userId the ID of the user editing a rating
     * @param movieId the movieID of the edited rating
     * @param newRating the new rating of the movie
     */
    public void editRating(int userId, String movieId, double newRating) {
        String id = userId + "";
        String ratingStr = newRating + "";
        executeQuery("UPDATE [Ratings] SET Rating = '" + ratingStr +
                "' WHERE [UserID] = '" + id + "' AND [MovieID] = '" + movieId + "'");
    }


    /**
     * Gets all ratings from the database
     * @return 2D List containing all of the ratings. The outer list contains all the rows
     * the inner list returns UserID, MovieID, and Rating in that order (as strings)
     */
    public List<List<String>> getRatingsTable() {
        ArrayList<List<String>> ratings = new ArrayList<>();
        String userId;
        String movieId;
        String rating;
        ResultSet rs = executeQuery("SELECT * FROM Ratings");

        try {
            do {
                ArrayList<String> ratingRow = new ArrayList<>();

                userId = rs.getString("userID");
                movieId = rs.getString("MovieID");
                rating = rs.getString("Rating");

                ratingRow.add(userId);
                ratingRow.add(movieId);
                ratingRow.add(rating);

                ratings.add(ratingRow);

            } while (rs.next());
        } catch (java.sql.SQLException e) {
            Log.d("getRatingsTable", e.getMessage());
        } catch (java.lang.NullPointerException e) {
            Log.d("getRatingsTable", "null pointer: " + e.getMessage());
        }

        return ratings;
    }


    /**
     * Returns a descending list of movie IDs that match the input major. If the major is null,
     * then it returns a list of all movies regardless of major
     * @param major the major to filter by
     * @return a list of movie IDs
     */
    public List<List<String>> getMovieRecsByMajor(String major) {
        String whereString = "";
        String query = "";
        ResultSet rs;
        List<List<String>> movies = new ArrayList<>();
        if (major != null) {
            whereString = "WHERE Users.Major = '" + major + "'";
        }
        query = "SELECT Ratings.MovieID, MAX(MovieTitle) AS MovieTitle FROM Ratings " +
                "INNER JOIN [Users] " +
                "ON Ratings.UserID = Users.UserID " +
                "INNER JOIN [Movies] " +
                "ON Ratings.MovieID = Movies.MovieID " +
                whereString + " " +
                "GROUP BY Ratings.MovieID " +
                "ORDER BY AVG(Rating) DESC";
        rs = executeQuery(query);

        try {
            do {
                List<String> movie = new ArrayList<>();

                movie.add(rs.getString("MovieTitle"));
                movie.add(rs.getString("MovieID"));

                movies.add(movie);

            } while (rs.next());
        } catch (Exception e) {
            Log.d("getMovieRecsByMajor", e.getMessage());
        }

        return movies;
    }


    /**
     * Adds movie to the database
     * @param movieId the String containing the OMDb id
     * @param movieTitle the movie title for the database
     * @param genre the genre list of the movie
     */
    public void addMovie(String movieId, String movieTitle, String genre) {
        movieTitle = sanitizeInput(movieTitle);
        executeQuery("INSERT INTO Movies " +
                "(MovieID, MovieTitle, Genre)" +
                " Values ('" + movieId + "', '" + movieTitle + "', '" + genre + "');");
    }

    /**
     * Returns a 2D list containing info on movies
     * Contains: MovieID, MovieTitle, Genre in that order for every row
     * @return
     */
    public List<List<String>> getMoviesTable() {
        ArrayList<List<String>> movies = new ArrayList<>();
        String movieID;
        String movieTitle;
        String genre;
        ResultSet rs = executeQuery("SELECT * FROM Movies");

        try {
            do {
                ArrayList<String> movieRow = new ArrayList<>();

                movieID = rs.getString("MovieID");
                movieTitle = rs.getString("MovieTitle");
                genre = rs.getString("Genre");

                movieRow.add(movieID);
                movieRow.add(movieTitle);
                movieRow.add(genre);

                movies.add(movieRow);

            } while (rs.next());
        } catch (java.sql.SQLException e) {
            Log.d("getMoviesTable", e.getMessage());
        } catch (java.lang.NullPointerException e) {
            Log.d("getMoviesTable", "null pointer: " + e.getMessage());
        }

        return movies;

    }

    /**
     * Sanitize input by escaping all the ' since this will mess with sql queries
     * @param input the string to be sanitized
     * @return the sanitized version of the string input
     */
    private String sanitizeInput(String input) {
        String cleanStr = "";
        cleanStr = input.replaceAll("'", "''");
        cleanStr = cleanStr.replaceAll(";", "");
        return cleanStr;
    }

}

