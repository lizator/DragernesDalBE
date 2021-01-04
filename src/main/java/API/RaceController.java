package API;

import dal.RaceDAO;
import dal.dto.InventoryDTO;
import dal.dto.RaceDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}
