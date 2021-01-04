package dal;

import dal.dto.EventDTO;
import dal.dto.InventoryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public List<EventDTO> getAllEvents() {
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.events", new String[] {});
            List<EventDTO> eventList = new ArrayList<>();
            while (rs.next()) {
                EventDTO eventDTO = new EventDTO();
                setEvent(rs, eventDTO);
                eventList.add(eventDTO);
            }
            rs.close();
            db.close();
            return eventList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with event");
            //throw new SQLException("Error in Database");
        }
    }

    private void setEvent(ResultSet rs, EventDTO eventDTO) throws SQLException {
        eventDTO.setEventID(rs.getInt("idevents"));
        eventDTO.setAddress(rs.getString("address"));
        eventDTO.setInfo(rs.getString("info"));
        eventDTO.setName(rs.getString("name"));
        eventDTO.setStartDate(rs.getDate("startDate"));
        eventDTO.setEndDate(rs.getDate("endDate"));
    }
}
