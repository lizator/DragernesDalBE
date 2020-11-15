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

    public List<CharacterDTO> getCharactersByUserID(int userID){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.character WHERE iduser = " + userID + " AND status = 'aktiv';");
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

    public CharacterDTO getCharacterByID(int characterID){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.character WHERE idcharacter = " + characterID + " AND status = 'aktiv';");
            List<CharacterDTO> charList = new ArrayList<>();
            rs.next();
            CharacterDTO character = new CharacterDTO();
            setCharacter(rs, character);
            charList.add(character);
            rs.close();
            db.close();
            return character;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public CharacterDTO createCharacter(CharacterDTO dto){
        try {
            dto.setIdcharacter(getNextID()); //Get ID assigned
            dto.setStatus("aktiv");
            db.connect();
            db.update("INSERT INTO companiondb.character (idcharater, iduser, " +
                    "namecharacter, idrace, age, status) VALUES ('"
                    + dto.getIdcharacter() + "', '"
                    + dto.getIduser() + "', '"
                    + dto.getName() + "', '"
                    + dto.getIdrace() + "', '"
                    + dto.getAge() + "', '"
                    + dto.getStatus() + "');");
            db.close();

            CharacterDTO character = getCharacterByID(dto.getIdcharacter());
            return character;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error in DB: Creating character");
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

    public int getNextID() throws WebApplicationException{ //Returns true if email already exists in system
        try {
            db.connect();
            ResultSet rs = db.query("SELECT MAX(idCharacter) AS max FROM companiondb.character;");
            rs.next();
            int max = rs.getInt("max");
            rs.close();
            db.close();
            return max + 1;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error in DB");
        }

    }

}
