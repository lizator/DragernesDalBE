package API;

import dal.*;
import dal.dto.MagicSchoolDTO;
import dal.dto.MagicTierDTO;
import dal.dto.SpellDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MagicController {
    SpellDAO spellDAO = new SpellDAO();
    MagicTierDAO tierDAO = new MagicTierDAO();
    MagicSchoolDAO schoolDAO = new MagicSchoolDAO();


    @GetMapping(value = "/magic/spells", produces = "application/json")
    public List<SpellDTO> getAlleSpells(){
        return spellDAO.getAllSpells();
    }

    @GetMapping(value = "/magic/tiers", produces = "application/json")
    public List<MagicTierDTO> getAllTiers(){
        return tierDAO.getAllTiers();
    }

    @GetMapping(value = "/magic/schools", produces = "application/json")
    public List<MagicSchoolDTO> getAllSchools(){
        return schoolDAO.getAllSchools();
    }


}
