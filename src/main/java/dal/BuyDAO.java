package dal;

import dal.dto.AbilityDTO;
import dal.dto.BuyDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.ArrayList;

public class BuyDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);
    CharacterDAO characterDAO = new CharacterDAO();
    AbilityDAO abilityDAO = new AbilityDAO();

    public BuyDTO buyAbility(BuyDTO buyDto){
        try {
            ArrayList<String> cmd = new ArrayList<>();
            for(AbilityDTO ability : buyDto.getAbilityBuyList()) {
                if ((!abilityDAO.getAbilityExist(ability.getId())) || ability.getType().equals("Magi")) {
                    if (ability.getId() == -3) { //generates new works and updates id
                        AbilityDTO work = abilityDAO.addCraft(ability.getName());
                        ability.setId(work.getId());
                        cmd.add("INSERT INTO companiondb.ownedabilities (idcharacter, idability) VALUES (" + buyDto.getCharacter().getIdcharacter() + "," + ability.getId() + "); ");
                    } else if (ability.getType().equals("Magi")){ //Magic tier disguised as ability (magicTierID == abilityID)
                        cmd.add("INSERT INTO companiondb.ownedspelltiers (idcharacter, idspelltier) VALUES (" + buyDto.getCharacter().getIdcharacter() + "," + ability.getId() + "); ");
                    }
                }
                else {
                    cmd.add("INSERT INTO companiondb.ownedabilities (idcharacter, idability) VALUES (" + buyDto.getCharacter().getIdcharacter() + "," + ability.getId() + "); ");
                }
            }
            db.connect();
            for (String c : cmd)
                db.update(c, new String[]{});
            db.close();
            return new BuyDTO(characterDAO.updateCharacter(buyDto.getCharacter()), buyDto.getAbilityBuyList());

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with ability");
            //throw new SQLException("Error in Database");
        }
    }
}
