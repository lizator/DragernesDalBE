package dal;

import dal.dto.AttendingDTO;
import dal.dto.EventDTO;
import dal.dto.InventoryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    private SQLDatabaseIO getDb() {
        return new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);
    }

    //private final SQLDatabaseIO db = new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);

    public List<EventDTO> getAllEvents() {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM events where startDate >= now();", new String[] {});
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
        eventDTO.setStartDate(rs.getTimestamp("startDate"));
        eventDTO.setEndDate(rs.getTimestamp("endDate"));
        eventDTO.setHyperlink(rs.getString("hyperlink"));
    }

    public EventDTO createEvent(EventDTO dto) {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("START TRANSACTION;",new String[]{});
            db.update("INSERT INTO events (name, startDate, endDate, address, info, hyperlink) VALUES (?,?,?,?,?, ?)",
                    new String[] {dto.getName()+"",dto.getStartDate()+"",dto.getEndDate()+"",dto.getAddress()+"",dto.getInfo()+"",dto.getHyperlink()+""});
            db.update("COMMIT", new String[]{});
            db.close();

            return new EventDTO();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB: creating event");
        }

    }

    public EventDTO editEvent(EventDTO dto) {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("UPDATE events SET " +
                    "name = ?, " +
                    "startDate = ?, " +
                    "endDate = ?, " +
                    "address = ?, " +
                    "info = ?, " +
                    "hyperlink = ?" +
                    "WHERE idevents = ?;", new String[]{
                    dto.getName()+"",
                    dto.getStartDate()+"",
                    dto.getEndDate()+"",
                    dto.getAddress()+"",
                    dto.getInfo()+"",
                    dto.getHyperlink()+"",
                    dto.getEventID()+""});
            db.close();
            System.out.println(dto.getStartDate());
            return dto;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB: creating event");
        }

    }

}
