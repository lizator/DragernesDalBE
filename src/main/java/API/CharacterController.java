package API;

import dal.CharacterDAO;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CharacterController {
    CharacterDAO dao = new CharacterDAO();
}
