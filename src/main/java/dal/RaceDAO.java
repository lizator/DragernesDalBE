package dal;


import dal.dto.CharacterDTO;
import dal.dto.RaceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RaceDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public List<RaceDTO> getRacesStandart(){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.races WHERE idrace < 11", new String[] {});
            List<RaceDTO> raceList = new ArrayList<>();
            while (rs.next()) {
                RaceDTO race = new RaceDTO();
                setRace(rs, race);
                raceList.add(race);
            }
            rs.close();
            db.close();
            return raceList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with Races");
            //throw new SQLException("Error in Database");
        }
    }

    public RaceDTO getRace(int raceID){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.races WHERE idrace = ?", new String[] {raceID + ""});
            RaceDTO race = new RaceDTO();
            rs.next();
            setRace(rs, race);
            rs.close();
            db.close();
            return race;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with Races");
            //throw new SQLException("Error in Database");
        }
    }

    private void setRace(ResultSet rs, RaceDTO race) throws SQLException { //TODO implement background
        race.setID(rs.getInt("idrace"));
        race.setRacename(rs.getString("racename"));
        race.setStart(rs.getInt("start"));
        race.setEp2(rs.getInt("2ep"));
        race.setEp3(rs.getInt("3ep"));
        race.setEp4(rs.getInt("4ep"));
        race.setDesc(rs.getString("desc"));
    }
}
