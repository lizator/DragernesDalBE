package dal;

import dal.dto.CharacterDTO;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterDAOTest {
    CharacterDAO dao = new CharacterDAO();
    @Test
    public void createCharacter() {
        CharacterDTO dto = new CharacterDTO();
        dto.setIdcharacter(dao.getNextID());
        dto.setName("Testing creation");
        dto.setStatus("aktiv");
        dto.setAge(72);
        dto.setIdrace(2); //Elver
        dto.setIduser(2); //test@gmail.com
        CharacterDTO ret = dao.createCharacter(dto);
        assertEquals(ret.getAge(), 72);
    }
}