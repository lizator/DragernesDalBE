package dal;

import dal.dto.MagicSchoolDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MagicSchoolDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);

    public List<MagicSchoolDTO> getAllSchools(){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.magicschools", new String[] {});
            List<MagicSchoolDTO> schoolList = new ArrayList<>();
            while (rs.next()) {
                MagicSchoolDTO school = new MagicSchoolDTO();
                setSchool(rs, school);
                schoolList.add(school);
            }
            rs.close();
            db.close();
            return schoolList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with spells");
            //throw new SQLException("Error in Database");
        }
    }

    private void setSchool(ResultSet rs, MagicSchoolDTO school) throws SQLException {
        school.setID(rs.getInt("idmagicschool"));
        school.setSchoolName(rs.getString("namemagicschool"));
        school.setLvl1ID(rs.getInt("lvl1id"));
        school.setLvl2ID(rs.getInt("lvl2id"));
        school.setLvl3ID(rs.getInt("lvl3id"));
        school.setLvl4ID(rs.getInt("lvl4id"));
        school.setLvl5ID(rs.getInt("lvl5id"));
        school.setDesc(rs.getString("descmagicschool"));
        school.setColor(rs.getString("colormagicschool"));
    }

}
