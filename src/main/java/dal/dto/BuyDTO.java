package dal.dto;

import java.util.ArrayList;

public class BuyDTO {
    CharacterDTO character;
    ArrayList<AbilityDTO> abilityBuyList;

    public BuyDTO(CharacterDTO character, ArrayList<AbilityDTO> abilityBuyList){
        this.character = character;
        this.abilityBuyList = abilityBuyList;
    }

    public CharacterDTO getCharacter() {
        return character;
    }

    public void setCharacter(CharacterDTO character) {
        this.character = character;
    }

    public ArrayList<AbilityDTO> getAbilityBuyList() {
        return abilityBuyList;
    }

    public void setAbilityBuyList(ArrayList<AbilityDTO> abilityBuyList) {
        this.abilityBuyList = abilityBuyList;
    }
}
