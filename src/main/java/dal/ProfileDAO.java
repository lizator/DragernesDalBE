package dal;

import dal.dto.ProfileDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public ProfileDAO() {
    }

    public ProfileDTO getProfileByEmail(String email){
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Error in DB");
            //throw new SQLException("Error in Database");
        }

    }

    public ProfileDTO updateUser(ProfileDTO dto){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT COUNT(*) as count FROM companiondb.user WHERE email = ? AND idUser != ?", new String[] {dto.getEmail(), dto.getId()+""});
            rs.next();
            int count = rs.getInt("count");
            rs.close();
            if (count != 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email ekstisterer allerede for en anden bruger");
            }
            int admin = 0;
            if (dto.isAdmin()) admin = 1;

            db.update("UPDATE companiondb.user SET " +
                            "firstName = ?, " +
                            "secondName = ?, " +
                            "email = ?, " +
                            "phone = ?, "+
                            "passHash = ?, "+
                            "salt = ?, "+
                            "admin = ? "+
                            "WHERE idUser = ?;",
                    new String[] {dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getPhone()+"", dto.getPassHash(), dto.getSalt(), admin+"", dto.getId()+""});
            ResultSet rs2 = db.query("SELECT * FROM user WHERE email = ?", new String[] {dto.getEmail()});
            rs2.next();
            ProfileDTO user = new ProfileDTO();
            setUser(rs2, user);
            rs2.close();
            db.close();
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Error in DB");
            //throw new SQLException("Error in Database");
        }

    }

    public boolean getEmailExists(String email){ //Returns true if email already exists in system
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with Email Exists");
            //throw new SQLException("Error in Database");
        }
    }

    // @TODO don't do this!!! Use auto increment in database instead.
    public int getNextID() { //Returns true if email already exists in system
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with userID");
            //throw new SQLException("Error in Database");
        }

    }

    public ProfileDTO createUser(ProfileDTO dto){
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with creating user");
            //throw new SQLException("Error in Database");
        }

    }



    public boolean getConnected() throws SQLException {
        db.connect();
        return true;
    }

    private void setUser(ResultSet rs, ProfileDTO user) throws SQLException { //TODO implement background
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

