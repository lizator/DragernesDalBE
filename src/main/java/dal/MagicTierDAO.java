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
                MagicTierDTO spell = new MagicTierDTO();
                setTier(rs, spell);
                tierList.add(spell);
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

    private void setTier(ResultSet rs, MagicTierDTO tier) throws SQLException {
        tier.setID(rs.getInt("idspellrelation"));
        tier.setLvl(rs.getInt("lvl"));
        tier.setSpell1ID(rs.getInt("spell1id"));
        tier.setSpell2ID(rs.getInt("spell2id"));
        try {
            tier.setSpell3ID(rs.getInt("spell3id"));
        } catch (SQLException e){
            tier.setSpell3ID(-1);
        }
        try {
            tier.setSpell4ID(rs.getInt("spell4id"));
        } catch (SQLException e){
            tier.setSpell4ID(-1);
        }
        try {
            tier.setSpell5ID(rs.getInt("spell5id"));
        } catch (SQLException e){
            tier.setSpell5ID(-1);
        }

    }
}
