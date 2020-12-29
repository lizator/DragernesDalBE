package API;

import dal.AbilityDAO;
import dal.CharacterDAO;
import dal.dto.AbilityDTO;
import dal.dto.CharacterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class AbilityController {
    AbilityDAO dao = new AbilityDAO();
    CharacterDAO characterDAO = new CharacterDAO();

    @GetMapping(value = "/ability/byCharacterID/{characterid}", produces = "application/json")
    public List<AbilityDTO> getAbilitiesByCharacterID(@PathVariable int characterid){
        return dao.getAbilitiesByCharacterID(characterid);
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



}
