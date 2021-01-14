package dal;

import dal.dto.InventoryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public List<InventoryDTO> getInventoryByCharacterIDWithUpdate(int characterid) {
        //return null;
        try {
            db.connect();
            ResultSet rsStatus = db.query("(SELECT COUNT(*) as count FROM companiondb.inventoryrelation WHERE idcharacter = ? AND Status = 'update')", new String[]{characterid+""});
            rsStatus.next();
            int count = rsStatus.getInt("count");
            rsStatus.close();
            String status;
            if (count == 0) status = "current";
            else status = "update";

            ResultSet rs = db.query("SELECT * FROM companiondb.inventoryrelation WHERE idcharacter = ? AND Status = ?", new String[]{characterid+"", status});
            rs.next();
            int idInventoryRelation = rs.getInt("idinventoryrelation");
            rs.close();

            ResultSet rs2 = db.query("SELECT * FROM companiondb.inventory WHERE idinventoryrelation = ?", new String[]{idInventoryRelation+""});
            List<InventoryDTO> itemList = new ArrayList<>();
            while (rs2.next()) {
                InventoryDTO item = new InventoryDTO();
                setItem(rs2, item);
                itemList.add(item);
            }
            rs2.close();
            db.close();
            return itemList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public List<InventoryDTO> getCurrentInventoryByCharacterID(int characterid) {
        //return null;
        try {
            db.connect();

            String status = "current";
            ResultSet rs = db.query("SELECT * FROM companiondb.inventoryrelation WHERE idcharacter = ? AND Status = ?", new String[]{characterid+"", status});
            rs.next();
            int idInventoryRelation = rs.getInt("idinventoryrelation");
            rs.close();

            ResultSet rs2 = db.query("SELECT * FROM companiondb.inventory WHERE idinventoryrelation = ?", new String[]{idInventoryRelation+""});
            List<InventoryDTO> itemList = new ArrayList<>();
            while (rs2.next()) {
                InventoryDTO item = new InventoryDTO();
                setItem(rs2, item);
                itemList.add(item);
            }
            rs2.close();
            db.close();
            return itemList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    public List<InventoryDTO> saveInventoryForUpdate(int characterid, ArrayList<InventoryDTO> inventory) {
        try {
            int relationID = getNextID();
            List<InventoryDTO> itemList = new ArrayList<>();
            db.connect();
            ResultSet rsStatus = db.query("(SELECT COUNT(*) as count FROM companiondb.inventoryrelation WHERE idcharacter = ? AND Status = 'update')", new String[]{characterid+""});
            rsStatus.next();
            int count = rsStatus.getInt("count");
            if (count == 0) {
                db.update("INSERT INTO companiondb.inventoryrelation (idinventoryrelation, idcharacter) VALUES (?,?)", new String[]{relationID+"", characterid+""});
            } else {
                ResultSet rsGetID = db.query("SELECT * FROM companiondb.inventoryrelation WHERE idcharacter = ? AND Status = 'update'", new String[]{characterid+""});
                rsGetID.next();
                relationID = rsGetID.getInt("idinventoryrelation");
                rsGetID.close();

                db.update("DELETE FROM companiondb.inventory WHERE idinventoryrelation = ?", new String[]{relationID+""});
            }

            for (InventoryDTO line : inventory){
                db.update("INSERT INTO companiondb.inventory (idinventoryrelation, iditem, itemname, amount) VALUES (?,?,?,?)",
                        new String[]{relationID+"", line.getIdItem()+"", line.getItemName(), line.getAmount()+""});
            }

            ResultSet rs2 = db.query("SELECT * FROM companiondb.inventory WHERE idinventoryrelation = ?", new String[]{relationID+""});
            while (rs2.next()) {
                InventoryDTO item = new InventoryDTO();
                setItem(rs2, item);
                itemList.add(item);
            }
            rs2.close();

            db.close();
            return itemList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with inventory");
            //throw new SQLException("Error in Database");
        }
    }

    public InventoryDTO getState(int relationID){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.inventoryrelation WHERE idinventoryrelation = ?", new String[]{relationID+""});
            rs.next();
            String status = rs.getString("Status");
            rs.close();
            db.close();
            InventoryDTO dto = new InventoryDTO();
            dto.setItemName(status);
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with inventory");
            //throw new SQLException("Error in Database");
        }
    }

    public void setupCharacterInventory(int characterid){
        try {
            int relationid = getNextID();
            db.connect();
            db.update("INSERT INTO companiondb.inventoryrelation (idinventoryrelation, idcharacter, Status) VALUES (?,?,'current')", new String[]{relationid+"", characterid+""});
            db.update("INSERT INTO companiondb.inventory (idinventoryrelation, iditem, itemname, amount) " +
                    "VALUES " +
                    "(?, 0, 'Guld', 0), " +
                    "(?, 1, 'Sølv', 0), " +
                    "(?, 2, 'Kobber', 0)",new String[]{relationid+"",relationid+"",relationid+""});
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with inventory");
            //throw new SQLException("Error in Database");
        }
    }

    public InventoryDTO denyCharacter(int characterid){
        try {
            db.connect();
            db.update("UPDATE companiondb.inventoryrelation SET " +
                    "Status = 'denied' " +
                    "WHERE idcharacter = ? AND Status = 'update';", new String[]{characterid+""});
            db.close();
            InventoryDTO dto = new InventoryDTO();
            dto.setAmount(1);
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with inventory");
            //throw new SQLException("Error in Database");
        }
    }

    public InventoryDTO denyAllRelations(){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.inventoryrelation WHERE Status = 'update'", new String[]{});
            while (rs.next()){
                db.update("UPDATE companiondb.inventoryrelation SET " +
                        "Status = 'denied' " +
                        "WHERE idinventoryrelation = ?;", new String[]{rs.getInt("idinventoryrelation")+""});
            }
            rs.close();
            db.close();
            InventoryDTO dto = new InventoryDTO();
            dto.setAmount(1);
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with inventory");
            //throw new SQLException("Error in Database");
        }
    }

    public InventoryDTO confirmRelation(int characterid){
        try {
            db.connect();

            ResultSet rsOld = db.query("SELECT * FROM companiondb.inventoryrelation WHERE idcharacter = ? AND Status = 'current'", new String[]{characterid+""});
            rsOld.next();
            int oldID = rsOld.getInt("idinventoryrelation");
            db.update("UPDATE companiondb.inventoryrelation SET " +
                    "Status = 'old' " +
                    "WHERE idinventoryrelation = ?;", new String[]{oldID+""});
            rsOld.close();

            ResultSet rsNew = db.query("SELECT * FROM companiondb.inventoryrelation WHERE idcharacter = ? AND Status = 'update'", new String[]{characterid+""});
            rsNew.next();
            int relationid = rsNew.getInt("idinventoryrelation");
            db.update("UPDATE companiondb.inventoryrelation SET " +
                    "Status = 'current' " +
                    "WHERE idinventoryrelation = ?;", new String[]{relationid+""});
            rsNew.close();

            db.close();
            InventoryDTO dto = new InventoryDTO();
            dto.setAmount(1);
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with inventory");
            //throw new SQLException("Error in Database");
        }
    }

    private int getNextID(){
        try {
            db.connect();
            ResultSet rs = db.query("SELECT MAX(idinventoryrelation) AS max FROM companiondb.inventoryrelation;", new String[]{});
            rs.next();
            int max = rs.getInt("max");
            rs.close();
            db.close();
            return max + 1;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error in DB with next idinventoryrelation");
            //throw new SQLException("Error in Database");
        }
    }

    private void setItem(ResultSet rs, InventoryDTO item) throws SQLException {
        item.setIdInventoryRelation(rs.getInt("idinventoryrelation"));
        item.setIdItem(rs.getInt("iditem"));
        item.setItemName(rs.getString("itemname"));
        item.setAmount(rs.getInt("amount"));
    }

}
