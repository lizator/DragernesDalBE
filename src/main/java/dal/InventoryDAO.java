package dal;

import dal.dto.InventoryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
    private SQLDatabaseIO getDb() {
        return new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);
    }

    //private final SQLDatabaseIO db = new SQLDatabaseIO("ybyfqrmupcyoxk", "11e2c72d61349e7579224313c650c39ef21fea976dea1428f0fe38201b624e28", "ec2-52-214-178-113.eu-west-1.compute.amazonaws.com", 5432);

    public List<InventoryDTO> getInventoryByCharacterIDWithUpdate(int characterid) {
        //return null;
        try {
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rsStatus = db.query("(SELECT COUNT(*) as count FROM inventoryrelation WHERE idcharacter = ? AND Status = 'update')", new String[]{characterid+""});
            rsStatus.next();
            int count = rsStatus.getInt("count");
            rsStatus.close();
            String status;
            if (count == 0) status = "current";
            else status = "update";

            ResultSet rs = db.query("SELECT * FROM inventoryrelation WHERE idcharacter = ? AND Status = ?", new String[]{characterid+"", status});
            rs.next();
            int idInventoryRelation = rs.getInt("idinventoryrelation");
            rs.close();

            ResultSet rs2 = db.query("SELECT * FROM inventory WHERE idinventoryrelation = ?", new String[]{idInventoryRelation+""});
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
            SQLDatabaseIO db = getDb();
            db.connect();

            String status = "current";
            ResultSet rs = db.query("SELECT * FROM inventoryrelation WHERE idcharacter = ? AND Status = ?", new String[]{characterid+"", status});
            rs.next();
            int idInventoryRelation = rs.getInt("idinventoryrelation");
            rs.close();

            ResultSet rs2 = db.query("SELECT * FROM inventory WHERE idinventoryrelation = ?", new String[]{idInventoryRelation+""});
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
            SQLDatabaseIO db = getDb();
            int relationID = getNextID();
            List<InventoryDTO> itemList = new ArrayList<>();
            db.connect();
            ResultSet rsStatus = db.query("(SELECT COUNT(*) as count FROM inventoryrelation WHERE idcharacter = ? AND Status = 'update')", new String[]{characterid+""});
            rsStatus.next();
            int count = rsStatus.getInt("count");
            if (count == 0) {
                db.update("INSERT INTO inventoryrelation (idinventoryrelation, idcharacter) VALUES (?,?)", new String[]{relationID+"", characterid+""});
            } else {
                ResultSet rsGetID = db.query("SELECT * FROM inventoryrelation WHERE idcharacter = ? AND Status = 'update'", new String[]{characterid+""});
                rsGetID.next();
                relationID = rsGetID.getInt("idinventoryrelation");
                rsGetID.close();

                db.update("DELETE FROM inventory WHERE idinventoryrelation = ?", new String[]{relationID+""});
            }

            for (InventoryDTO line : inventory){
                db.update("INSERT INTO inventory (idinventoryrelation, iditem, itemname, amount) VALUES (?,?,?,?)",
                        new String[]{relationID+"", line.getIdItem()+"", line.getItemName(), line.getAmount()+""});
            }

            ResultSet rs2 = db.query("SELECT * FROM inventory WHERE idinventoryrelation = ?", new String[]{relationID+""});
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
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT * FROM inventoryrelation WHERE idinventoryrelation = ?", new String[]{relationID+""});
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
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("INSERT INTO inventoryrelation (idinventoryrelation, idcharacter, Status) VALUES (?,?,'current')", new String[]{relationid+"", characterid+""});
            db.update("INSERT INTO inventory (idinventoryrelation, iditem, itemname, amount) " +
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
            SQLDatabaseIO db = getDb();
            db.connect();
            //Really nice sql!
            db.update("delete inventoryrelation, inventory from inventoryrelation " +
                    "inner join inventory on inventoryrelation.idinventoryrelation = inventory.idinventoryrelation" +
                    " where idcharacter = ? and Status = 'update';", new String[]{characterid+""});
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
            SQLDatabaseIO db = getDb();
            db.connect();
            db.update("delete inventoryrelation, inventory from inventoryrelation " +
                    "inner join inventory on inventoryrelation.idinventoryrelation = inventory.idinventoryrelation" +
                    " and Status = 'update';", new String[]{});
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
            SQLDatabaseIO db = getDb();
            db.connect();

            ResultSet rsOld = db.query("SELECT * FROM inventoryrelation WHERE idcharacter = ? AND Status = 'current'", new String[]{characterid+""});
            int oldID = -1;
            if(rsOld.next()){
                oldID = rsOld.getInt("idinventoryrelation");
                db.update("UPDATE inventoryrelation SET " +
                        "Status = 'old' " +
                        "WHERE idinventoryrelation = ?;", new String[]{oldID+""});
                rsOld.close();
            }


            ResultSet rsNew = db.query("SELECT * FROM inventoryrelation WHERE idcharacter = ? AND Status = 'update'", new String[]{characterid+""});
            rsNew.next();
            int relationid = rsNew.getInt("idinventoryrelation");
            db.update("UPDATE inventoryrelation SET " +
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
            SQLDatabaseIO db = getDb();
            db.connect();
            ResultSet rs = db.query("SELECT MAX(idinventoryrelation) AS max FROM inventoryrelation;", new String[]{});
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
