package dal;

import dal.dto.AbilityDTO;
import dal.dto.EventDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbilityDAO {

    private SQLDatabaseIO getDb() {
        return new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);
    }

    //private final SQLDatabaseIO db = new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);

    public List<AbilityDTO> getAbilitiesByCharacterID(int characterid) {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM owningAbilitiesView WHERE idcharacter = ?", new String[] {characterid+""});
            return getAbilityDTOS(rs, db);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public List<AbilityDTO> getAllUnCommonAbilities(){
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM abilities WHERE type != 'Viden' AND type != 'Sniger' AND type != 'Allemand' AND type != 'Kamp' AND type != 'Bad' AND type != 'Håndværk';", new String[] {});
            return getAbilityDTOS(rs, db);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public List<AbilityDTO> getAll(){
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM abilities;", new String[] {});
            return getAbilityDTOS(rs, db);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    private List<AbilityDTO> getAbilityDTOS(ResultSet rs, SQLDatabaseIO db) throws SQLException {
        List<AbilityDTO> abilityList = new ArrayList<>();
        while(rs.next()){
            AbilityDTO ability = new AbilityDTO();
            setAbility(rs, ability);
            abilityList.add(ability);
        }
        rs.close();
        db.close();
        return abilityList;
    }

    public List<AbilityDTO> getAbilitiesByRaceID(int raceID) {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM races WHERE idrace = ?", new String[] {raceID+""});
            List<AbilityDTO> abilityList = new ArrayList<>();
            ArrayList<Integer> idList = new ArrayList<>();
            rs.next();
            for (int i = 0; i < 4; i++){
                idList.add(rs.getInt(i+3));
            }
            rs.close();
            db.close();
            for(int abilityID : idList){
                db.connect();
                ResultSet rs2 = db.query("SELECT * FROM abilities WHERE idability = ?", new String[] {abilityID+""});
                rs2.next();
                AbilityDTO ability = new AbilityDTO();
                setAbility(rs2, ability);
                rs2.close();
                abilityList.add(ability);
                db.close();
            }
            return abilityList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public List<AbilityDTO> getRacestartersAbilities() {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM abilities WHERE cost = 0 AND type = 'Race' AND idability < 39", new String[] {});
            return getAbilityDTOS(rs, db);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public List<AbilityDTO> getAbilitiesByType(String type) {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM abilities WHERE type = ? ORDER BY nameability", new String[] {type});
            return getAbilityDTOS(rs, db);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public AbilityDTO getAbilityByID(int abilityID){
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM abilities WHERE idability = ? ", new String[] {abilityID+""});
            rs.next();
            AbilityDTO ability = new AbilityDTO();
            setAbility(rs, ability);
            rs.close();
            db.close();
            return ability;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public boolean getAbilityExist(int abilityID){
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT count(*) AS exist FROM abilities WHERE idability = ? ", new String[] {abilityID+""});
            rs.next();
            int exist = rs.getInt("exist");
            rs.close();
            db.close();
            if (exist == 0){
                return false;
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public AbilityDTO buyAbility(int characterID, int abilityID){
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("INSERT INTO ownedabilities (idcharacter, idability) VALUES (?,?)",
                    new String[]{characterID+"",abilityID+""});
            db.close();
            return getAbilityByID(abilityID);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with ability");
            //throw new SQLException("Error in Database");
        }
    }

    public AbilityDTO addCraft(String craft){
        try {
            SQLDatabaseIO db = getDb();
            AbilityDTO dto = new AbilityDTO(getNextID(), craft, 1, "Dette er et Håndværk", "Håndværk", 0, "NULL", 0);
            db.connect();
            db.update("INSERT INTO abilities (idability, nameability, cost, type, pagenumber, shortdesc, command) VALUES (?,?,?,?,?,?,?)",
                    new String[]{dto.getId()+"",dto.getName(),
                    dto.getCost()+"", dto.getType(), dto.getPagenumber()+"",
                    dto.getDesc(), dto.getCommand()});
            db.close();
            return getAbilityByID(dto.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with ability");
            //throw new SQLException("Error in Database");
        }
    }

    public ArrayList<String> getTypes(){
        ArrayList<String> types = new ArrayList<>();
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT DISTINCT abilities.type FROM abilities;", new String[]{});
            while (rs.next()) {
                types.add(rs.getString("type"));
            }
            rs.close();
            db.close();
            return types;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with types");
            //throw new SQLException("Error in Database");
        }
    }

    public AbilityDTO createAbility(AbilityDTO dto) {
        try {
            int nextID = getNextID();
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("START TRANSACTION;",new String[]{});
            db.update("INSERT INTO abilities (idability, nameability, cost, type, shortdesc) VALUES (?,?,?,?,?)",
                    new String[] {String.valueOf(nextID), dto.getName(),dto.getCost()+"",dto.getType(),dto.getDesc()});
            db.update("COMMIT", new String[]{});
            db.close();

            return new AbilityDTO();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB: creating event");
        }
    }

    public AbilityDTO updateAbility(AbilityDTO dto) {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("START TRANSACTION;",new String[]{});
            db.update("UPDATE abilities SET " +
                            "nameability = ?, " +
                            "cost = ?, " +
                            "type = ?, " +
                            "shortdesc = ? "+
                            "WHERE idability = ?;",
                    new String[] {dto.getName(),dto.getCost()+"",dto.getType(),dto.getDesc(),dto.getId()+""});
            db.update("COMMIT", new String[]{});
            db.close();

            return new AbilityDTO();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB: creating event");
        }
    }

    public List<AbilityDTO> setAbilities(int characterid, ArrayList<AbilityDTO> abilities) {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("START TRANSACTION;",new String[]{});
            db.update("DELETE FROM ownedabilities WHERE idcharacter = ?", new String[]{characterid+""});
            for (AbilityDTO dto : abilities){
                db.update("INSERT INTO ownedabilities (idcharacter, idability) VALUES (?,?)",
                        new String[]{characterid+"",dto.getId()+""});
            }
            db.update("COMMIT", new String[]{});
            db.close();
            return getAbilitiesByCharacterID(characterid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB: creating event");
        }
    }

    public int getNextID(){
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT MAX(idability) AS max FROM abilities;", new String[]{});
            rs.next();
            int max = rs.getInt("max");
            rs.close();
            db.close();
            return max + 1;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with next idability");
            //throw new SQLException("Error in Database");
        }
    }

    private void setAbility(ResultSet rs, AbilityDTO ability) throws SQLException {
        ability.setId(rs.getInt("idability"));
        ability.setName(rs.getString("nameability"));
        ability.setCost(rs.getInt("cost"));
        ability.setType(rs.getString("type"));
        ability.setPagenumber(rs.getInt("pagenumber"));
        ability.setDesc(rs.getString("shortdesc"));
        ability.setCommand(rs.getString("command"));
        ability.setIdparent(rs.getInt("idparentability"));
    }
}
