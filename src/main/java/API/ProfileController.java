package API;

import dal.ProfileDAO;
import dal.dto.ProfileDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    ProfileDAO dao = new ProfileDAO();

    @PostMapping(value = "/user/getbyemail", consumes = "application/json", produces = "application/json")
    public ProfileDTO getProfileByEmail(@RequestBody ProfileDTO dto) {
        return dao.getProfileByEmail(dto.getEmail());
    }


}
