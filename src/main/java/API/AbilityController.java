package API;

import dal.AbilityDAO;
import dal.CharacterDAO;
import dal.dto.AbilityDTO;
import dal.dto.CharacterDTO;
import dal.dto.EventDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AbilityController {
    AbilityDAO dao = new AbilityDAO();
    CharacterDAO characterDAO = new CharacterDAO();

    @GetMapping(value = "/ability/byCharacterID/{characterid}", produces = "application/json")
    public List<AbilityDTO> getAbilitiesByCharacterID(@PathVariable int characterid){
        return dao.getAbilitiesByCharacterID(characterid);
    }

    @GetMapping(value = "/ability/byRaceID/{raceID}", produces = "application/json")
    public List<AbilityDTO> getAbilitiesByRaceID(@PathVariable int raceID){
        return dao.getAbilitiesByRaceID(raceID);
    }

    @GetMapping(value = "/ability/getTypes", produces = "application/json")
    public List<String> getTypes(){
        return dao.getTypes();
    }

    @GetMapping(value = "/ability/all", produces = "application/json")
    public List<AbilityDTO> getAll(){
        return dao.getAll();
    }

    @PostMapping(value = "/ability/edit", consumes = "application/json", produces = "application/json")
    public AbilityDTO editEvent(@RequestBody AbilityDTO dto) {
        return dao.updateAbility(dto);
    }

    @PostMapping(value = "/ability/set/{characterid}", consumes = "application/json", produces = "application/json")
    public List<AbilityDTO> setAbilities(@PathVariable int characterid, @RequestBody ArrayList<AbilityDTO> abilties) {
        return dao.setAbilities(characterid, abilties);
    }



    @GetMapping(value = "/ability/allUnCommonAbilities", produces = "application/json")
    public List<AbilityDTO> getAllUncommonAbilities(){
        return dao.getAllUnCommonAbilities();
    }

    @GetMapping(value = "/ability/raceStaters", produces = "application/json")
    public List<AbilityDTO> getRacestartersAbilities(){
        return dao.getRacestartersAbilities();
    }

    @GetMapping(value = "/ability/byType/{type}", produces = "application/json")
    public List<AbilityDTO> getAbilitiesByRaceID(@PathVariable String type){
        return dao.getAbilitiesByType(type);
    }

    @GetMapping(value = "/ability/byID/{abilityID}", produces = "application/json")
    public AbilityDTO getAbilityByID(@PathVariable int abilityID){
        return dao.getAbilityByID(abilityID);
    }

    @GetMapping(value = "/ability/buy/{characterID}/{abilityID}", produces = "application/json")
    public AbilityDTO buyAbility(@PathVariable int characterID, @PathVariable int abilityID){

        CharacterDTO characterDTO = characterDAO.getCharacterByID(characterID); //getting character
        int cost = dao.getAbilityByID(abilityID).getCost();                     //getting cost for new ability

        //test for if able to buy (security)
        if (cost > characterDTO.getCurrentep()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough EP");
        
        characterDTO.setCurrentep(characterDTO.getCurrentep() - cost);          //setting new EP
        characterDAO.updateCharacter(characterDTO);                             //saving character
        AbilityDTO dto = dao.buyAbility(characterID, abilityID);                //adding ability to character
        return dto;
    }

    @GetMapping(value = "/ability/getfree/{characterID}/{abilityID}", produces = "application/json")
    public AbilityDTO getFreeAbility(@PathVariable int characterID, @PathVariable int abilityID){
        AbilityDTO dto = dao.buyAbility(characterID, abilityID);                //adding ability to character
        return dto;
    }

    @GetMapping(value = "/ability/buyAndGetFree/{characterID}/{abilityID}/{freeAbilityID}", produces = "application/json")
    public List<AbilityDTO> buyAndGetFreeAbility(@PathVariable int characterID, @PathVariable int abilityID, @PathVariable int freeAbilityID){
        ArrayList<AbilityDTO> ls = new ArrayList<>();
        ls.add(buyAbility(characterID, abilityID));
        ls.add(getFreeAbility(characterID, freeAbilityID));
        return ls;
    }

    @GetMapping(value = "/ability/freeAndGetFree/{characterID}/{abilityID}/{freeAbilityID}", produces = "application/json")
    public List<AbilityDTO> freeAndGetFreeAbility(@PathVariable int characterID, @PathVariable int abilityID, @PathVariable int freeAbilityID){
        ArrayList<AbilityDTO> ls = new ArrayList<>();
        ls.add(getFreeAbility(characterID, abilityID));
        ls.add(getFreeAbility(characterID, freeAbilityID));
        return ls;
    }

    @PostMapping(value = "/ability/create", consumes = "application/json", produces = "application/json")
    public AbilityDTO createEvent(@RequestBody AbilityDTO dto) {
        return dao.createAbility(dto);
    }

    @PostMapping(value = "/ability/craft/{characterID}", produces = "application/json")
    public AbilityDTO buyAbility(@PathVariable int characterID, @RequestBody AbilityDTO craft){

        CharacterDTO characterDTO = characterDAO.getCharacterByID(characterID); //getting character
        int cost = 1;                     //getting cost for new ability

        //test for if able to buy (security)
        if (cost > characterDTO.getCurrentep()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough EP");

        characterDTO.setCurrentep(characterDTO.getCurrentep() - cost);          //setting new EP
        characterDAO.updateCharacter(characterDTO);                             //saving character
        AbilityDTO newdto = dao.addCraft(craft.getName());                                //adding new craft to DB
        AbilityDTO dto = dao.buyAbility(characterID, newdto.getId());           //adding ability to character
        return dto;
    }

}
