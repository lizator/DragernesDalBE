package dal;

import dal.dto.MainDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public MainDTO getLastTimeTableModified(String tableName){
        try {
            db.connect();
            ResultSet rs = db.query("SHOW TABLE STATUS FROM companiondb LIKE ?",new String[]{tableName});
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
