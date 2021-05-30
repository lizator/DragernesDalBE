package dal;

import dal.dto.AbilityDTO;
import dal.dto.BuyDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

public class BuyDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);
    CharacterDAO characterDAO = new CharacterDAO();

    public BuyDTO buyAbility(BuyDTO buyDto){
        try {
            db.connect();
            String cmd = "";
            for(AbilityDTO ability : buyDto.getAbilityBuyList()) {
                cmd += "INSERT INTO companiondb.ownedabilities (idcharacter, idability) VALUES (" + buyDto.getCharacter().getIdcharacter() + "," + ability.getId() + "); ";
            }
            db.update(cmd, new String[]{});
            db.close();
            return new BuyDTO(characterDAO.updateCharacter(buyDto.getCharacter()), buyDto.getAbilityBuyList());

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with ability");
            //throw new SQLException("Error in Database");
        }
    }
}
