package dal;

import dal.dto.AttendingDTO;
import dal.dto.EventDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendingDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public List<AttendingDTO> getAllAttending(int charID) {
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.eventAttendancyList WHERE idcharacter = ?", new String[] {charID+""});
            List<AttendingDTO> attendingList = new ArrayList<>();
            while (rs.next()) {
                AttendingDTO attendingDTO = new AttendingDTO();
                setAttending(rs, attendingDTO);
                attendingList.add(attendingDTO);
            }
            rs.close();
            db.close();
            return attendingList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with event");
            //throw new SQLException("Error in Database");
        }
    }

    private void setAttending(ResultSet rs, AttendingDTO attendingDTO) throws SQLException {
        attendingDTO.setIdChar(rs.getInt("idcharacter"));
        attendingDTO.setIdEvent(rs.getInt("idevent"));
    }
}
