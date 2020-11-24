package dal;

import dal.dto.ProfileDTO;

import javax.ws.rs.WebApplicationException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public ProfileDAO() {
    }

    public ProfileDTO getProfileByEmail(String email) throws WebApplicationException{
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM user WHERE email = ?", new String[] { email });
            rs.next();
            ProfileDTO user = new ProfileDTO();
            setUser(rs, user);
            rs.close();
            db.close();
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error in DB");
            //throw new SQLException("Error in Database");
        }

    }

    public boolean getEmailExists(String email) throws WebApplicationException{ //Returns true if email already exists in system
        try {
            db.connect();
            ResultSet rs = db.query("SELECT COUNT(*) AS 'count' FROM user WHERE email = ?", new String[] { email });
            rs.next();
            int count = rs.getInt("count");
            rs.close();
            db.close();
            return (count != 0);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error in DB");
            //throw new SQLException("Error in Database");
        }
    }

    // @TODO don't do this!!! Use auto increment in database instead.
    public int getNextID() throws WebApplicationException{ //Returns true if email already exists in system
        try {
            db.connect();
            ResultSet rs = db.query("SELECT MAX(idUser) AS max FROM user", new String[] {});
            rs.next();
            int max = rs.getInt("max");
            rs.close();
            db.close();
            return max + 1;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error in DB");
            //throw new SQLException("Error in Database");
        }

    }

    public ProfileDTO createUser(ProfileDTO dto) throws WebApplicationException{
        try {
            dto.setId(getNextID()); //Get ID assigned
            db.connect();
            db.update("INSERT INTO user (idUser, firstName, secondName, email, phone, passHash, salt) VALUES (?,?,?,?,?,?,?)",
                    new String[]{dto.getId()+"",dto.getFirstName(),dto.getLastName(),dto.getEmail(),dto.getPhone()+"",dto.getPassHash(),dto.getSalt()});

            ProfileDTO user = getProfileByEmail(dto.getEmail());
            db.close();
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error in DB");
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

