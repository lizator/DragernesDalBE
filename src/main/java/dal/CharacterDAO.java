package dal;

import dal.dto.CharacterDTO;
import dal.dto.ProfileDTO;

import javax.ws.rs.WebApplicationException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CharacterDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public CharacterDAO(){}

    public List<CharacterDTO> getCharactersByID(int id){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM character WHERE iduser = " + id + " AND status = 'aktiv';");
            List<CharacterDTO> charList = new ArrayList<>();
            while (rs.next()) {
                CharacterDTO character = new CharacterDTO();
                setCharacter(rs, character);
                charList.add(character);
            }
            rs.close();
            db.close();
            return charList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }



    private void setCharacter(ResultSet rs, CharacterDTO character) throws SQLException {
        character.setIduser(rs.getInt("iduser"));
        character.setIdcharacter(rs.getInt("idcharacter"));
        character.setName(rs.getString("namecharacter"));
        character.setIdrace(rs.getInt("idrace"));
        character.setAge(rs.getInt("age"));
        character.setCurrentep(rs.getInt("currentep"));
        character.setStatus(rs.getString("status"));
        character.setDate(rs.getDate("date"));
        character.setTimestamp(rs.getTime("timestamp"));
        character.setStrength(rs.getInt("strength"));
        character.setHealth(rs.getInt("health"));
    }

}
