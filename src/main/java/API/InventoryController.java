package API;

import dal.InventoryDAO;
import dal.dto.InventoryDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class InventoryController {
    InventoryDAO dao = new InventoryDAO();

    @GetMapping(value = "/inventory/actualByCharacterID/{characterid}", produces = "application/json") //Works
    public List<InventoryDTO> getInvetory(@PathVariable int characterid){
        return dao.getInventoryByCharacterIDWithUpdate(characterid);
    }

    @GetMapping(value = "/inventory/currentByCharacterID/{characterid}", produces = "application/json")
    public List<InventoryDTO> getCurrentInvetory(@PathVariable int characterid){
        return dao.getCurrentInventoryByCharacterID(characterid);
    }

    @GetMapping(value = "/inventory/state/{relationid}", produces = "application/json")
    public String getState(@PathVariable int relationid){
        return dao.getState(relationid);
    }

    @PostMapping(value = "/inventory/save/{characterID}", consumes = "application/json", produces = "application/json")
    public List<InventoryDTO> saveInvetory(@PathVariable int characterid, @RequestBody ArrayList<InventoryDTO> inventory){
        return dao.saveInventoryForUpdate(characterid, inventory);
    }

    @GetMapping(value = "/inventory/deny/{relationid}", produces = "application/json")
    public boolean deny(@PathVariable int relationid){
        return dao.denyRelation(relationid);
    }

    @GetMapping(value = "/inventory/denyall", produces = "application/json")
    public boolean denyAll(){
        return dao.denyAllRelations();
    }

    @GetMapping(value = "/inventory/confirm/{characterid}", produces = "application/json")
    public boolean confirm(@PathVariable int characterid){
        return dao.confirmRelation(characterid);
    }

}
