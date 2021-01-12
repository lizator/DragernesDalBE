package dal;

import dal.dto.MagicTierDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MagicTierDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public List<MagicTierDTO> getAllTiers(){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.spelltiers", new String[] {});
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
            ResultSet rs = db.query("SELECT * FROM companiondb.ownedspelltiers", new String[] {});
            ArrayList<Integer> tierIDs = new ArrayList<>();
            while(rs.next()){
                tierIDs.add(rs.getInt("idspelltier"));
            }
            rs.close();
            List<MagicTierDTO> tierList = new ArrayList<>();
            for (int id : tierIDs){
                ResultSet rs2 = db.query("SELECT * FROM companiondb.spelltiers WHERE idtier = ?", new String[] {id+""});
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
