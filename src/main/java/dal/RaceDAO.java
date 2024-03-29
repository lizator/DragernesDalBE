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
    private SQLDatabaseIO getDb() {
        return new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);
    }

    //private final SQLDatabaseIO db = new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);

    public List<RaceDTO> getRacesStandartDeprecated() { //Deprecated, should get < 11. Here for old version of app.
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM races WHERE idrace < 10", new String[]{});
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

    public List<RaceDTO> getRacesStandart() { //Deprecated, should get < 11. Here for old version of app.
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM races WHERE idrace < 11", new String[]{});
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

    public List<RaceDTO> getCustomRaces() {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM races WHERE idrace > 11", new String[]{});
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

    public RaceDTO updateRace(RaceDTO dto) {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("START TRANSACTION;", new String[]{});
            db.update("UPDATE races SET " +
                    "racename = ?, " +
                    "start = ?, " +
                    "twoep = ?, " +
                    "threeep = ?, " +
                    "fourep = ?" +
                    "WHERE idrace = ?;", new String[]{
                    dto.getRacename() + "",
                    dto.getStart() + "",
                    dto.getEp2() + "",
                    dto.getEp3() + "",
                    dto.getEp4() + "",
                    dto.getID() + ""});
            db.update("COMMIT;", new String[]{});
            db.close();
            return dto;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB: creating character");
            //throw new SQLException("Error in Database");
        }
    }

    public RaceDTO createRace(RaceDTO dto) {
        try {
            int nextId = getNextID();
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("START TRANSACTION;", new String[]{});
            db.update("INSERT INTO races (idrace, racename, start, twoep, threeep, fourep)" +
                    " VALUES (?,?,?,?,?,?)", new String[]{String.valueOf(nextId), dto.getRacename() + "", dto.getStart() + "", dto.getEp2() + "", dto.getEp3() + "", dto.getEp4() + ""});
            db.update("COMMIT;", new String[]{});
            db.close();
            return dto;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB: creating character");
            //throw new SQLException("Error in Database");
        }
    }

    public RaceDTO getRace(int raceID) {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM races WHERE idrace = ?", new String[]{raceID + ""});
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

    public List<RaceDTO> getCharacterRaces(int characterID) {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs0 = db.query("SELECT COUNT(*) AS count FROM krysling WHERE idcharacter = ?", new String[]{characterID + ""});
            rs0.next();
            if (rs0.getInt("count") == 1) {
                ResultSet rs = db.query("SELECT * FROM krysling WHERE idcharacter = ?", new String[]{characterID + ""});
                List<RaceDTO> raceList = new ArrayList<>();
                rs.next();
                int race1ID = rs.getInt("race1");
                int race2ID = rs.getInt("race2");
                raceList.add(getRace(race1ID));
                raceList.add(getRace(race2ID));
                rs0.close();
                rs.close();
                db.close();
                return raceList;
            }
            db.close();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "character not in Krysling-table");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with krysling");
            //throw new SQLException("Error in Database");
        }
    }

    public int getNextID() {
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT MAX(idrace) AS max FROM races;", new String[]{});
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

    private void setRace(ResultSet rs, RaceDTO race) throws SQLException { //TODO implement background
        race.setID(rs.getInt("idrace"));
        race.setRacename(rs.getString("racename"));
        race.setStart(rs.getInt("start"));
        race.setEp2(rs.getInt("twoep"));
        race.setEp3(rs.getInt("threeep"));
        race.setEp4(rs.getInt("fourep"));
    }
}
