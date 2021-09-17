package dal;

import dal.dto.AbilityDTO;
import dal.dto.MagicTierDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MagicTierDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);

    public List<MagicTierDTO> getAllTiers(){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM d4t0u63k7aqlao.spelltiers", new String[] {});
            List<MagicTierDTO> tierList = new ArrayList<>();
            while (rs.next()) {
                MagicTierDTO tier = new MagicTierDTO();
                setTier(rs, tier);
                tierList.add(tier);
            }
            rs.close();
            db.close();
            return tierList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with spells");
            //throw new SQLException("Error in Database");
        }
    }

    public List<MagicTierDTO> getTiersByCharID(int characterID){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM d4t0u63k7aqlao.ownedspelltiers WHERE idcharacter = ?", new String[] {characterID+""});
            ArrayList<Integer> tierIDs = new ArrayList<>();
            while(rs.next()){
                tierIDs.add(rs.getInt("idspelltier"));
            }
            rs.close();
            List<MagicTierDTO> tierList = new ArrayList<>();
            for (int id : tierIDs){
                ResultSet rs2 = db.query("SELECT * FROM d4t0u63k7aqlao.spelltiers WHERE idtier = ?", new String[] {id+""});
                rs2.next();
                MagicTierDTO dto = new MagicTierDTO();
                setTier(rs2, dto);
                tierList.add(dto);
                rs2.close();
            }

            db.close();
            return tierList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with spells");
            //throw new SQLException("Error in Database");
        }
    }

    public MagicTierDTO insertTierBought(int characterid, int tierid){
        try {
            db.connect();
            db.update("INSERT INTO d4t0u63k7aqlao.ownedspelltiers (idcharacter, idspelltier) VALUES (?,?)",
                    new String[]{characterid+"",tierid+""});
            db.close();
            return getByID(tierid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with spells");
            //throw new SQLException("Error in Database");
        }
    }

    public List<MagicTierDTO> setCharacterMagic(int characterid, ArrayList<MagicTierDTO> tierList){
        try {
            db.connect();
            db.update("START TRANSACTION;",new String[]{});
            db.update("DELETE FROM d4t0u63k7aqlao.ownedspelltiers WHERE idcharacter = ?", new String[]{characterid+""});
            for (MagicTierDTO dto : tierList){
                db.update("INSERT INTO d4t0u63k7aqlao.ownedspelltiers (idcharacter, idspelltier) VALUES (?,?)",
                        new String[]{characterid+"",dto.getID()+""});
            }
            db.update("COMMIT", new String[]{});
            db.close();
            return getTiersByCharID(characterid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB: creating event");
        }
    }

    public MagicTierDTO getByID(int tierID){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM d4t0u63k7aqlao.spelltiers WHERE idtier = ?", new String[] {tierID+""});
            rs.next();
            MagicTierDTO dto = new MagicTierDTO();
            setTier(rs, dto);
            rs.close();
            db.close();
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with spells");
            //throw new SQLException("Error in Database");
        }
    }

    private void setTier(ResultSet rs, MagicTierDTO tier) throws SQLException {
        tier.setID(rs.getInt("idtier"));
        tier.setLvl(rs.getInt("lvl"));
        tier.setSpell1ID(rs.getInt("spell1id"));
        tier.setSpell2ID(rs.getInt("spell2id"));
        tier.setSpell3ID(rs.getInt("spell3id"));
        tier.setSpell4ID(rs.getInt("spell4id"));
        tier.setSpell5ID(rs.getInt("spell5id"));
    }
}
