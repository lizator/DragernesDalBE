package API;

import dal.InventoryDAO;
import dal.dto.InventoryDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InventoryController {
    InventoryDAO dao = new InventoryDAO();

    @GetMapping(value = "/inventory/byCharacterID/{characterid}", produces = "application/json") //Works
    public List<InventoryDTO> getCharacterByID(@PathVariable int characterid){
        return dao.getInventoryByCharacterID(characterid);
    }
}
