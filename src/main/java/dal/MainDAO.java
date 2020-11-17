package dal;

import dal.dto.ProfileDTO;

import javax.ws.rs.WebApplicationException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public String getLastTimeTableModified(String tableName){
        try {
            db.connect();
            ResultSet rs = db.query("SHOW TABLE STATUS FROM companiondb LIKE '" + tableName + "';");
            rs.next();
            String update = rs.getString("Update_time");
            if (update == null) update = rs.getString("Create_time");
            rs.close();
            db.close();
            return update;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error in DB");
            //throw new SQLException("Error in Database");
        }
    }
}
