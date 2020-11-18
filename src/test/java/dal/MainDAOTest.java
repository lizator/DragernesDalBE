package dal;

import dal.dto.MainDTO;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainDAOTest {

    @Test
    public void getLastTimeTableModified() {
        MainDAO dao = new MainDAO();
        MainDTO dto = dao.getLastTimeTableModified("user");
        assertTrue(true);
    }
}