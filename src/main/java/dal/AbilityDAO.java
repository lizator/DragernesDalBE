package dal;

import dal.dto.AbilityDTO;
import dal.dto.CharacterDTO;

import javax.ws.rs.WebApplicationException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbilityDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public List<AbilityDTO> getAbilitiesByCharacterID(int characterid) {
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.owningAbilitiesView WHERE idcharacter = ?", new String[] {characterid+""});
            List<AbilityDTO> abilityList = new ArrayList<>();
            while (rs.next()) {
                AbilityDTO ability = new AbilityDTO();
                setAbility(rs, ability);
                abilityList.add(ability);
            }
            rs.close();
            db.close();
            return abilityList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error in DB with character");
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
    }
}
