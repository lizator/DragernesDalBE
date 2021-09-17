package dal;

import dal.dto.SpellDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpellDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);

    public List<SpellDTO> getAllSpells(){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM spells", new String[] {});
            List<SpellDTO> spellList = new ArrayList<>();
            while (rs.next()) {
                SpellDTO spell = new SpellDTO();
                setSpell(rs, spell);
                spellList.add(spell);
            }
            rs.close();
            db.close();
            return spellList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with spells");
            //throw new SQLException("Error in Database");
        }
    }

    private void setSpell(ResultSet rs, SpellDTO spell) throws SQLException { //TODO implement background
        spell.setID(rs.getInt("idspells"));
        spell.setSpellname(rs.getString("namespell"));
        spell.setDesc(rs.getString("descspell"));
        spell.setItem(rs.getString("item"));
        spell.setDuration(rs.getString("duration"));
    }

}
