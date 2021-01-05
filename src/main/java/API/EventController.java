package API;

import dal.AttendingDAO;
import dal.EventDAO;
import dal.dto.AttendingDTO;
import dal.dto.EventDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController {
    EventDAO dao = new EventDAO();
    AttendingDAO daoA = new AttendingDAO();

    @GetMapping(value = "/event/events", produces = "application/json") //Works
    public List<EventDTO> getAllEvents(){
        return dao.getAllEvents();
    }
    @GetMapping(value = "/event/attending/{charID}", produces = "application/json")
    public List<AttendingDTO> getAllAttending(@PathVariable int charID) {return daoA.getAllAttending(charID);}
}
