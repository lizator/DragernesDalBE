package API;

import dal.ProfileDAO;
import dal.dto.ProfileDTO;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.WebApplicationException;

@RestController
public class ProfileController {
    ProfileDAO dao = new ProfileDAO();

    @PostMapping(value = "/user/getbyemail", consumes = "application/json", produces = "application/json")
    public ProfileDTO getProfileByEmail(@RequestBody ProfileDTO dto) throws WebApplicationException {
        return dao.getProfileByEmail(dto.getEmail());
    }

    @PostMapping(value = "/user/create", consumes = "application/json", produces = "application/json")
    public ProfileDTO createUser(@RequestBody ProfileDTO dto) throws WebApplicationException {
        if (!dao.getEmailExists(dto.getEmail())) {
            System.out.println("In here");
            System.out.println(dao.getEmailExists(dto.getEmail()));
            return dao.createUser(dto);
        }
        throw new WebApplicationException("Email already exists in database", 409);
    }


}
