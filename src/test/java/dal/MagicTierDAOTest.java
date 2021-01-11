package dal;

import org.junit.Test;

import static org.junit.Assert.*;

public class MagicTierDAOTest {

    @Test
    public void getAllTiers() {
        MagicTierDAO dao = new MagicTierDAO();
        dao.getAllTiers();
    }
}