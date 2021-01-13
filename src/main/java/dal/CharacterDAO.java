package dal;

import dal.dto.CharacterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
            ResultSet rs = db.query("SELECT * FROM companiondb.characterInfoView WHERE iduser = ? AND status = 'aktiv'", new String[] {userID+""});
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public List<CharacterDTO> getCharactersByEventID(int eventID, int checkedIn){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.characterInfoView inner join companiondb.eventAttendancyList" +
                    " on companiondb.eventAttendancyList.idcharacter = companiondb.characterInfoView.idcharacter where companiondb.eventAttendancyList.idevent = ? and eventAttendancyList.checkIn = ?;", new String[] {eventID+"",checkedIn+""});
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }



    public CharacterDTO insertKrysling(int characterid, int race1id, int race2id){
        try {
            db.connect();
            db.update("INSERT INTO companiondb.krysling (idcharacter, race1, race2) " +
                    "VALUES (?, ?, ?)", new String[] {characterid+"", race1id+"", race2id+""});
            db.close();
            return getCharacterByID(characterid);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public CharacterDTO getCharacterByID(int characterID){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.characterInfoView WHERE idcharacter = ?", new String[] {characterID+""});
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }
 //TODO remove
    public CharacterDTO updateCharacter(CharacterDTO dto){
        try {
            db.connect();
            db.update("UPDATE companiondb.character SET " +
                    "iduser = ?, " +
                    "namecharacter = ?, " +
                    "idrace = ?, " +
                    "age = ?, " +
                    "currentep = ?, " +
                    "strength = ?, " +
                    "health = ?, " +
                    "background = ? " +
                    "WHERE idcharacter = ?;", new String[]{
                            dto.getIduser()+"",
                            dto.getName(),
                            dto.getIdrace()+"",
                            dto.getAge()+"",
                            dto.getCurrentep()+"",
                            dto.getStrength()+"",
                            dto.getHealth()+"",
                            dto.getBackground()+"",
                            dto.getIdcharacter()+""});
            return dto;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public CharacterDTO createCharacter(CharacterDTO dto){
        try {
            dto.setIdcharacter(getNextID()); //Get ID assigned
            db.connect();
            db.update("START TRANSACTION;",new String[]{});
            db.update("INSERT INTO companiondb.character (idcharacter, iduser, " +
                    "namecharacter, idrace, age, background) VALUES (?, ?, ?, ?, ?, ?)",new String[] {dto.getIdcharacter()+"",dto.getIduser()+"",dto.getName(),dto.getIdrace()+"",dto.getAge()+"",dto.getBackground()});
            db.update("INSERT INTO companiondb.inventory (iditem, idcharacter, itemname, amount) " +
                    "VALUES " +
                    "(1, ?, 'Guld', 0), " +
                    "(2, ?, 'Sølv', 0), " +
                    "(3, ?, 'Kobber', 0)",new String[]{dto.getIdcharacter()+"",dto.getIdcharacter()+"",dto.getIdcharacter()+""});
            //out-comment getting ability
            /*ResultSet rs = db.query("SELECT * FROM companiondb.races WHERE idrace = ?", new String[] {dto.getIdrace()+""});
            rs.next();
            int startingAbilityID = rs.getInt("start");
            db.update("INSERT INTO companiondb.ownedabilities (idcharacter, idability) VALUES (?, ?)",new String[]{dto.getIdcharacter()+"",startingAbilityID+""});*/
            db.update("COMMIT;", new String[]{});
            db.close();

            CharacterDTO character = getCharacterByID(dto.getIdcharacter());
            return character;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB: creating character");
            //throw new SQLException("Error in Database");
        }
    }


    private void setCharacter(ResultSet rs, CharacterDTO character) throws SQLException {
        character.setIduser(rs.getInt("iduser"));
        character.setIdcharacter(rs.getInt("idcharacter"));
        character.setName(rs.getString("namecharacter"));
        character.setIdrace(rs.getInt("idrace"));
        character.setRaceName(rs.getString("racename"));
        character.setAge(rs.getInt("age"));
        character.setCurrentep(rs.getInt("currentep"));
        character.setStrength(rs.getInt("strength"));
        character.setHealth(rs.getInt("health"));
        character.setBackground(rs.getString("background"));
    }

    public int getNextID(){ //Returns true if email already exists in system
        try {
            db.connect();
            ResultSet rs = db.query("SELECT MAX(idCharacter) AS max FROM companiondb.character;",new String[]{});
            rs.next();
            int max = rs.getInt("max");
            rs.close();
            db.close();
            return max + 1;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with characterID");
        }

    }

}
