package API;

import com.fasterxml.jackson.annotation.JsonFormat;
import dal.AttendingDAO;
import dal.CheckInDAO;
import dal.EventDAO;
import dal.dto.AttendingDTO;
import dal.dto.CharacterDTO;
import dal.dto.CheckInDTO;
import dal.dto.EventDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
    EventDAO dao = new EventDAO();
    AttendingDAO daoA = new AttendingDAO();
    CheckInDAO daoC = new CheckInDAO();

    @GetMapping(value = "/event/events", produces = "application/json") //Works
    public List<EventDTO> getAllEvents(){
        return dao.getAllEvents();
    }
    @GetMapping(value = "/event/attending/{charID}", produces = "application/json")
    public List<AttendingDTO> getAllAttending(@PathVariable int charID) {return daoA.getAllAttending(charID);}
    @PostMapping(value = "/event/attending/set", consumes = "application/json", produces = "application/json")
    public AttendingDTO setAttending(@RequestBody AttendingDTO dto){
        return daoA.setAttending(dto);
    }
    @PostMapping(value = "/event/attending/remove", consumes = "application/json", produces = "application/json")
    public AttendingDTO removeAttending(@RequestBody AttendingDTO dto){
        return daoA.removeAttending(dto);
    }
    @PostMapping(value = "/event/create", consumes = "application/json", produces = "application/json")
    public EventDTO createEvent(@RequestBody EventDTO dto){
        return dao.createEvent(dto);
    }
    @PostMapping(value = "/event/edit", consumes = "application/json", produces = "application/json")
    public EventDTO editEvent(@RequestBody EventDTO dto){
        return dao.editEvent(dto);
    }
    @PostMapping(value = "/event/checkin/set", consumes = "application/json", produces = "application/json")
    public CheckInDTO setCheckin(@RequestBody CheckInDTO dto){
        return daoC.setCharacterByEventID(dto);
    }

}
