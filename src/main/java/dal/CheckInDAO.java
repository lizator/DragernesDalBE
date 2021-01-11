package dal;

import dal.dto.AttendingDTO;
import dal.dto.CheckInDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CheckInDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public CheckInDTO setCharacterByEventID(CheckInDTO checkInDTO){
        try {
            db.connect();
            db.update("START TRANSACTION;",new String[]{});
            db.update("UPDATE companiondb.eventAttendancyList SET checkIn = ? WHERE idcharacter = ? AND idevent = ?", new String[] {checkInDTO.getCheckin()+"", checkInDTO.getIdChar()+"",checkInDTO.getIdEvent()+""});
            db.update("COMMIT", new String[]{});
            db.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
        return checkInDTO;
    }
}
