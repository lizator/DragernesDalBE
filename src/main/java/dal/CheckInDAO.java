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
    private SQLDatabaseIO getDb() {
        return new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);
    }

    //private final SQLDatabaseIO db = new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);

    public CheckInDTO setCharacterByEventID(CheckInDTO checkInDTO){
        try {
            String checkin = "false";
            if (checkInDTO.getCheckin() == 1) checkin = "true";
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("START TRANSACTION;",new String[]{});
            db.update("UPDATE eventAttendancyList SET checkIn = ? WHERE idcharacter = ? AND idevent = ?", new String[] {checkin, checkInDTO.getIdChar()+"",checkInDTO.getIdEvent()+""});
            db.update("COMMIT", new String[]{});
            db.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
        return checkInDTO;
    }

    public CheckInDTO addEP(CheckInDTO dto){
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("START TRANSACTION;",new String[]{});
            db.update("update character as t1, eventAttendancyList as t2 SET t1.currentep = currentep +1 WHERE t1.idcharacter = t2.idcharacter and t2.checkIn = true and t2.idevent = ?;",
                    new String[] {dto.getIdEvent()+""});
            db.update("COMMIT", new String[]{});
            db.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
        return dto;
    }
}
