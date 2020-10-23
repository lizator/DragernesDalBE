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

    @Test
    public void getEmailExists() {
        boolean testTrue = dao.getEmailExists("test@gmail.com");
        boolean testFalse = dao.getEmailExists("notEvenMail.com");
        assertTrue(testTrue);
        assertFalse(testFalse);
    }

    @Test
    public void getNextID() {
        int test = dao.getNextID();
        assertEquals(3, test);
    }

    @Test
    public void createUser() {
        ProfileDTO test = new ProfileDTO(0, "name1", "name2", "test2@gmail.com", 12342413, "nope", "Salt", false);
        test = dao.createUser(test);
        assertEquals(3, test.getId());
        assertEquals("name1", test.getFirstName());
    }
}