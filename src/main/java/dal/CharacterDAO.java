package dal;

import dal.dto.AbilityDTO;
import dal.dto.BuyDTO;
import dal.dto.CharacterDTO;
import dal.dto.RaceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CharacterDAO {
    private SQLDatabaseIO getDb() {
        return new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);
    }

    //private final SQLDatabaseIO db = new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);
    InventoryDAO inventoryDAO = new InventoryDAO();
    RaceDAO raceDAO = new RaceDAO();
    AbilityDAO abilityDAO = new AbilityDAO();

    public CharacterDAO(){}

    public List<CharacterDTO> getCharactersByUserID(int userID){
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM characterInfoView WHERE iduser = ?", new String[] {String.valueOf(userID)});
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
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM characterInfoView inner join eventAttendancyList" +
                    " on eventAttendancyList.idcharacter = characterInfoView.idcharacter where eventAttendancyList.idevent = ? and eventAttendancyList.checkIn = ?;", new String[] {eventID+"",checkedIn+""});
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
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("INSERT INTO krysling (idcharacter, race1, race2) " +
                    "VALUES (?, ?, ?)", new String[] {characterid+"", race1id+"", race2id+""});
            db.close();
            return getCharacterByID(characterid);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public List<RaceDTO> updateKrysling(int characterid, int race1id, int race2id){
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("UPDATE krysling SET race1 = ?, race2 = ? WHERE idcharacter = ?",
                    new String[] { race1id+"", race2id+"", characterid+""});
            db.close();
            RaceDAO raceDAO = new RaceDAO();
            return raceDAO.getCharacterRaces(characterid);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public CharacterDTO deleteKrysling(int characterid){
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("DELETE FROM krysling WHERE idcharacter = ?", new String[] {characterid+""});
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
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM characterInfoView WHERE idcharacter = ?", new String[] {characterID+""});
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
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("UPDATE character SET " +
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
            db.close();
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
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("START TRANSACTION;",new String[]{});
            db.update("INSERT INTO character (idcharacter, iduser, " +
                    "namecharacter, idrace, age, background) VALUES (?, ?, ?, ?, ?, ?)",new String[] {dto.getIdcharacter()+"",dto.getIduser()+"",dto.getName(),dto.getIdrace()+"",dto.getAge()+"",dto.getBackground()});
            db.update("COMMIT;", new String[]{});
            db.close();

            inventoryDAO.setupCharacterInventory(dto.getIdcharacter());
            CharacterDTO character = getCharacterByID(dto.getIdcharacter());
            return character;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB: creating character");
            //throw new SQLException("Error in Database");
        }
    }

    public BuyDTO createCharacterAndGetAbility(CharacterDTO dto){
        //Creating Character as normal
        CharacterDTO characterDTO = createCharacter(dto);

        //Finding starting ability
        RaceDTO race = raceDAO.getRace(dto.getIdrace());
        AbilityDTO ability = abilityDAO.getAbilityByID(race.getStart());

        //Adding ability to a BuyDTO for transport
        BuyDTO buyDTO = new BuyDTO(characterDTO, new ArrayList<AbilityDTO>());
        buyDTO.getAbilityBuyList().add(ability);

        return buyDTO;
    }

    public CharacterDTO deleteCharacter(int characterid){
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("DELETE FROM character WHERE idcharacter = ?",new String[] {characterid+""});
            db.close();
            CharacterDTO character = new CharacterDTO();
            character.setIdcharacter(1);
            return character;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB: deleting character");
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
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT MAX(idCharacter) AS max FROM character;",new String[]{});
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
