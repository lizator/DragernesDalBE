package dal;

import dal.dto.InventoryDTO;
import org.junit.Test;

import java.util.ArrayList;

public class InventoryDAOTest {
    InventoryDAO dao = new InventoryDAO();
    @Test
    public void saveInventory() { //WORKS
        ArrayList<InventoryDTO> lst = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            InventoryDTO dto = new InventoryDTO(0, i, "name"+i, i + 7);
            lst.add(dto);
        }
        dao.saveInventoryForUpdate(119, lst);
    }

    @Test
    public void setupTest() { //WORKS
        dao.setupCharacterInventory(0);
    }
}