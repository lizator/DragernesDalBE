package dal;

import dal.dto.SpellDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpellDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public List<SpellDTO> getAllSpells(){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.spells", new String[] {});
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
        spell.setDesc(rs.getString("duration"));
    }

}
