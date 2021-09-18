package API;

import dal.CharacterDAO;
import dal.dto.BuyDTO;
import dal.dto.CharacterDTO;
import dal.dto.RaceDTO;
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

    @GetMapping(value = "/character/byEventID/{eventid}/{checkin}", produces = "application/json")
    public List<CharacterDTO> getCharacterByEventID(@PathVariable int eventid, @PathVariable int checkin){
        return dao.getCharactersByEventID(eventid,checkin);
    }


    @PostMapping(value = "/character/create", consumes = "application/json", produces = "application/json")
    public CharacterDTO createCharacter(@RequestBody CharacterDTO dto){
        return dao.createCharacter(dto);
    }

    @PostMapping(value = "/character/createAndGetAbility", consumes = "application/json", produces = "application/json")
    public BuyDTO createCharacterAndGetAbility(@RequestBody CharacterDTO dto){
        return dao.createCharacterAndGetAbility(dto);
    }

    @GetMapping(value = "/character/krys/{characterid}/{race1id}/{race2id}", produces = "application/json")
    public CharacterDTO insertKrysling(@PathVariable int characterid, @PathVariable int race1id, @PathVariable int race2id){
        return dao.insertKrysling(characterid, race1id, race2id);
    }

    @GetMapping(value = "/character/updatekrys/{characterid}/{race1id}/{race2id}", produces = "application/json")
    public List<RaceDTO> updateKrysling(@PathVariable int characterid, @PathVariable int race1id, @PathVariable int race2id){
        return dao.updateKrysling(characterid, race1id, race2id);
    }

    @GetMapping(value = "/character/deletekrys/{characterid}", produces = "application/json")
    public CharacterDTO deleteKrysling(@PathVariable int characterid){
        return dao.deleteKrysling(characterid);
    }

    @PostMapping(value = "/character/update", consumes = "application/json", produces = "application/json")
    public CharacterDTO updateCharacter(@RequestBody CharacterDTO dto){
        return dao.updateCharacter(dto);
    }

    @GetMapping(value = "/character/delete/{characterid}", produces = "application/json")
    public CharacterDTO deleteCharacter(@PathVariable int characterid){
        return dao.deleteCharacter(characterid);
    }
}
