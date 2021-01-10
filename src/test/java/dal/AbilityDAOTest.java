package dal;

import dal.dto.AbilityDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AbilityDAOTest {
    private AbilityDAO dao = new AbilityDAO();

    @Test
    public void byRaceIDTest(){
        ArrayList<AbilityDTO> res = (ArrayList<AbilityDTO>) dao.getAbilitiesByRaceID(2);
        assertEquals(7, res.get(0).getId());
    }

}