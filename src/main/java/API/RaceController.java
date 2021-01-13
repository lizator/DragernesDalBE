package API;

import dal.RaceDAO;
import dal.dto.CharacterDTO;
import dal.dto.InventoryDTO;
import dal.dto.RaceDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RaceController {
    RaceDAO dao = new RaceDAO();

    @GetMapping(value = "/race/info/standart", produces = "application/json")
    public List<RaceDTO> getRacesStandart(){
        return dao.getRacesStandart();
    }

    @GetMapping(value = "/race/info/single/{raceID}", produces = "application/json")
    public RaceDTO getRaceinfo(@PathVariable int raceID){
        return dao.getRace(raceID);
    }

    @GetMapping(value = "/race/krys/getCharacterRaces/{characterid}", produces = "application/json")
    public List<RaceDTO> getCharacterRaces(@PathVariable int characterid){
        return dao.getCharacterRaces(characterid);
    }

    @PostMapping(value = "/race/create", consumes = "application/json", produces = "application/json")
    public RaceDTO createRace(@RequestBody RaceDTO dto){
        return dao.createRace(dto);
    }

}
