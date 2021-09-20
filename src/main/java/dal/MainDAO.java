package dal;

import dal.dto.MainDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainDAO {
    private SQLDatabaseIO getDb() {
        return new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);
    }

    //private final SQLDatabaseIO db = new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);

    public MainDTO getLastTimeTableModified(String tableName){
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SHOW TABLE STATUS FROM d4t0u63k7aqlao LIKE ?",new String[]{tableName});
            rs.next();
            String update = rs.getString("Update_time");
            if (update == null) update = rs.getString("Create_time");
            rs.close();
            db.close();
            return new MainDTO(update);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with main/table");
            //throw new SQLException("Error in Database");
        }
    }
}
