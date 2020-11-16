package API;

import dal.AbilityDAO;
import dal.dto.AbilityDTO;
import dal.dto.CharacterDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AbilityController {
    AbilityDAO dao = new AbilityDAO();

    @GetMapping(value = "/ability/byCharacterID/{characterid}", produces = "application/json")
    public List<AbilityDTO> getCharacterByID(@PathVariable int characterid){
        return dao.getabilitiesByCharacterID(characterid);
    }


}
