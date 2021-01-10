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
        dto.setName("Testing creation2");
        dto.setAge(72);
        dto.setIdrace(2); //Elver
        dto.setIduser(2); //test@gmail.com
        dto.setBackground("Testing ting and stuff");
        CharacterDTO ret = dao.createCharacter(dto);
        assertEquals(ret.getAge(), 72);
    }

    @Test
    public void updateCharacter() {
        CharacterDTO dto = dao.getCharacterByID(11);
        dto.setBackground("test Works");
        CharacterDTO ret = dao.updateCharacter(dto);
        assertEquals(ret.getBackground(), "test Works");
    }
}