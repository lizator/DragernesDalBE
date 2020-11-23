package dal;

import dal.dto.AbilityDTO;
import dal.dto.InventoryDTO;

import javax.ws.rs.WebApplicationException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {
    private final SQLDatabaseIO db = new SQLDatabaseIO("kamel", "dreng", "runerne.dk", 8003);

    public List<InventoryDTO> getInventoryByCharacterID(int characterid) {
        try {
            db.connect();
            ResultSet rs = db.query("SELECT * FROM companiondb.inventory WHERE idcharacter = ?", new String[]{characterid+""});
            List<InventoryDTO> itemList = new ArrayList<>();
            while (rs.next()) {
                InventoryDTO item = new InventoryDTO();
                setItem(rs, item);
                itemList.add(item);
            }
            rs.close();
            db.close();
            return itemList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error in DB with character");
            //throw new SQLException("Error in Database");
        }
    }

    private void setItem(ResultSet rs, InventoryDTO item) throws SQLException {
        item.setIdItem(rs.getInt("iditem"));
        item.setIdCharacter(rs.getInt("idcharacter"));
        item.setItemName(rs.getString("itemname"));
        item.setAmount(rs.getInt("amount"));
    }

}
