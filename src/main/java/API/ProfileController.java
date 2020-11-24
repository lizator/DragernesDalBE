package API;

import dal.ProfileDAO;
import dal.dto.ProfileDTO;
import org.springframework.web.bind.annotation.*;
import tools.PasswordHandler;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;

@RestController
public class ProfileController { //TODO implement sessions
    ProfileDAO dao = new ProfileDAO();
    PasswordHandler passHandler = new PasswordHandler();

    @PostMapping(value = "/user/getbyemail", consumes = "application/json", produces = "application/json")
    public ProfileDTO getProfileByEmail(@RequestBody ProfileDTO dto) throws WebApplicationException {
        return dao.getProfileByEmail(dto.getEmail());
    }

    @PostMapping(value = "/user/login", consumes = "application/json", produces = "application/json")
    public ProfileDTO login(@RequestBody ProfileDTO dto) throws WebApplicationException {
        ProfileDTO foundDto = dao.getProfileByEmail(dto.getEmail());
        if (passHandler.checkPass(dto.getPassHash(), foundDto.getPassHash(), foundDto.getSalt()))
            return foundDto;
        throw new WebApplicationException("incorrect password", 401); //unauthorized
    }

    @PostMapping(value = "/user/autologin", consumes = "application/json", produces = "application/json")
    public ProfileDTO autoLogin(@RequestBody ProfileDTO dto) throws WebApplicationException {
        ProfileDTO foundDto = dao.getProfileByEmail(dto.getEmail());
        if (foundDto.getPassHash().equals(dto.getPassHash()))
            return foundDto;
        throw new WebApplicationException("password changed", 401); //unauthorized
    }

    @PostMapping(value = "/user/create", consumes = "application/json", produces = "application/json")
    public ProfileDTO createUser(@RequestBody ProfileDTO dto) throws WebApplicationException {
        if (!dao.getEmailExists(dto.getEmail())) {
            ArrayList<String> passList = passHandler.encryptPassword(dto.getPassHash());
            dto.setPassHash(passList.get(0));
            dto.setSalt(passList.get(1));
            return dao.createUser(dto);
        }
        throw new WebApplicationException("Email already exists in database", 409);
    }


}
