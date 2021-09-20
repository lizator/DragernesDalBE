package API;

import dal.*;
import dal.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MagicController {
    SpellDAO spellDAO = new SpellDAO();
    MagicTierDAO tierDAO = new MagicTierDAO();
    CharacterDAO characterDAO = new CharacterDAO();
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

    @GetMapping(value = "/magic/bycharid/{characterID}", produces = "application/json")
    public List<MagicTierDTO> getTiersByCharacterID(@PathVariable int characterID){
        return tierDAO.getTiersByCharID(characterID);
    }

    @GetMapping(value = "/magic/buy/{characterid}/{tierid}/{cost}", produces = "application/json")
    public MagicTierDTO buyTier(@PathVariable int characterid, @PathVariable int tierid, @PathVariable int cost){
        CharacterDTO characterDTO = characterDAO.getCharacterByID(characterid); //getting character

        //test for if able to buy (security)
        if (cost > characterDTO.getCurrentep()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough EP");

        characterDTO.setCurrentep(characterDTO.getCurrentep() - cost);          //setting new EP
        characterDAO.updateCharacter(characterDTO);

        return tierDAO.insertTierBought(characterid, tierid);
    }

    @GetMapping(value = "/magic/getfree/{characterid}/{tierid}", produces = "application/json")
    public MagicTierDTO getFreeTier(@PathVariable int characterid, @PathVariable int tierid){
        return tierDAO.insertTierBought(characterid, tierid);
    }

    @PostMapping(value = "/magic/set/{characterid}", consumes = "application/json", produces = "application/json")
    public List<MagicTierDTO> setCharacterMagic(@PathVariable int characterid, @RequestBody ArrayList<MagicTierDTO> tierList){
        return tierDAO.setCharacterMagic(characterid, tierList);
    }




}
