package dal;

import dal.dto.ProfileDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public ProfileDAO() {
    }

    public ProfileDTO getProfileByEmail(String email){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM user WHERE email = '" + email + "';");
            rs.next();
            ProfileDTO user = new ProfileDTO();
            setUser(rs, user);
            rs.close();
            db.close();
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
            //throw new SQLException("Error in Database");
        }

    }

    public boolean getConnected() throws SQLException {
        db.connect();
        return true;
    }

    private void setUser(ResultSet rs, ProfileDTO user) throws SQLException {
        user.setId(rs.getInt("idUser"));
        user.setFirstName(rs.getString("firstName"));
        user.setLastName(rs.getString("secondName"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getInt("phone"));
        user.setPassHash(rs.getString("passHash"));
        user.setSalt(rs.getString("salt"));
        user.setAdmin(rs.getBoolean("admin"));
    }

}

