package dal;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.*;

public class SQLDatabaseIO {
    private final String DatabaseURL;
    private final String USER;
    private final String PASS;
    private String db_name = "companiondb";
    private boolean connected = false;
    private Connection conn = null;
    private Statement stmt = null;

    /**
     * Forbindelses parametre for databasen
     * @param USER Bruger Navn
     * @param PASSWORD Password
     * @param URL URL adressen
     * @param PORT Port nummer til database serveren
     */
    public SQLDatabaseIO(String USER, String PASSWORD, String URL, int PORT) {
        this.USER = USER;
        this.PASS = PASSWORD;
        this.DatabaseURL = "jdbc:mysql://" + URL + ":"+PORT+"/"+db_name+"?characterEncoding=latin1&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    }

    /**
     * Returnerer connected variabel
     * @return Returnerer connected variabel
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Try to connect to DB
     * @throws SQLException
     */
    public void connect() throws SQLException {
        if(!connected){
            String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
            try {
                Class.forName(JDBC_DRIVER);
            } catch (ClassNotFoundException e) {
                throw new SQLException();
            }
            conn = DriverManager.getConnection(DatabaseURL, USER, PASS);
            connected = true;
        }
    }

    /**
     * Runs update on mysql server.
     * @param query Database navn
     * @throws SQLException
     */
    public void update(String query, String[] strings) throws SQLException {
        if(connected){
            stmt = conn.createStatement();
            stmt.executeUpdate("use "+db_name);
            PreparedStatement stmt = conn.prepareStatement(query); // SELECT * FROM user WHERE email = ?
            for (int i = 0; i < strings.length; i++) {
                stmt.setString(i + 1, strings[i]);
            }
            System.out.println(stmt.toString());
            stmt.executeUpdate();
        }
    }


    /**
     * Runs query on mysql server, and returns ResultSet object.
     * @param query efterspørgesels string til database
     * @return Runs query on mysql server, and returns ResultSet object.
     * @throws SQLException
     */
    public ResultSet query(String query, String[] strings) throws SQLException {
        ResultSet result = null;
        if(!connected){
            System.out.println("Connect to a DB first");
        } else{
            PreparedStatement stmt = conn.prepareStatement(query); // SELECT * FROM user WHERE email = ?
            for (int i = 0; i < strings.length; i++) {
                stmt.setString(i + 1, strings[i]);
            }

            result = stmt.executeQuery();
        }
        return result;
    }


    /**
     * Close connection to mysql server
     * @throws SQLException
     */
    public void close() throws SQLException {
        if(connected){
            conn.close();
            connected=false;
        }
    }

}
