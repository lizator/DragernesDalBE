package API;

import dal.EventDAO;
import dal.InventoryDAO;
import dal.dto.EventDTO;
import dal.dto.InventoryDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController {
    EventDAO dao = new EventDAO();

    @GetMapping(value = "/event/events/", produces = "application/json") //Works
    public List<EventDTO> getCharacterByID(){
        return dao.getAllEvents();
    }
}
