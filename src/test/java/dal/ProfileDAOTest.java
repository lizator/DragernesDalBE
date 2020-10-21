package dal;

import dal.dto.ProfileDTO;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProfileDAOTest {
    ProfileDAO dao = new ProfileDAO();
    ProfileDTO testuser;

    @Test
    public void getProfileByEmail() {
        try {
            testuser = dao.getProfileByEmail("test@gmail.com");
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertEquals(2, testuser.getId());
    }
}