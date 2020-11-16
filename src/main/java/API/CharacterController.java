package API;

import dal.CharacterDAO;
import dal.dto.CharacterDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CharacterController {
    CharacterDAO dao = new CharacterDAO();

    @GetMapping(value = "/character/byID/{characterid}", produces = "application/json") //Works
    public CharacterDTO getCharacterByID(@PathVariable int characterid){
        return dao.getCharacterByID(characterid);
    }

    @GetMapping(value = "/character/byUserID/{userid}", produces = "application/json")
    public List<CharacterDTO> getCharacterByUserID(@PathVariable int userid){
        return dao.getCharactersByUserID(userid);
    }

    @PostMapping(value = "/character/create", consumes = "application/json", produces = "application/json")
    public CharacterDTO createCharacter(@RequestBody CharacterDTO dto){
        return dao.createCharacter(dto);
    }
}
