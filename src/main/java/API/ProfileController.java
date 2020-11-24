package API;

import dal.ProfileDAO;
import dal.dto.ProfileDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tools.PasswordHandler;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;

@RestController
public class ProfileController { //TODO implement sessions
    ProfileDAO dao = new ProfileDAO();
    PasswordHandler passHandler = new PasswordHandler();

    @PostMapping(value = "/user/getbyemail", consumes = "application/json", produces = "application/json")
    public ProfileDTO getProfileByEmail(@RequestBody ProfileDTO dto) throws WebApplicationException {
        if (dao.getEmailExists(dto.getEmail())) {
            return dao.getProfileByEmail(dto.getEmail());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not found");
    }

    @PostMapping(value = "/user/login", consumes = "application/json", produces = "application/json")
    public ProfileDTO login(@RequestBody ProfileDTO dto) throws WebApplicationException {
        if (dao.getEmailExists(dto.getEmail())) {
            ProfileDTO foundDto = dao.getProfileByEmail(dto.getEmail());
            if (passHandler.checkPass(dto.getPassHash(), foundDto.getPassHash(), foundDto.getSalt()))
                return foundDto;
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not found");
    }

    @PostMapping(value = "/user/autologin", consumes = "application/json", produces = "application/json")
    public ProfileDTO autoLogin(@RequestBody ProfileDTO dto) throws WebApplicationException {
        if (dao.getEmailExists(dto.getEmail())) {
            ProfileDTO foundDto = dao.getProfileByEmail(dto.getEmail());
            if (foundDto.getPassHash().equals(dto.getPassHash()))
                return foundDto;
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password doesn't match");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not found");
    }

    @PostMapping(value = "/user/create", consumes = "application/json", produces = "application/json")
    public ProfileDTO createUser(@RequestBody ProfileDTO dto) throws ResponseStatusException {
        if (!dao.getEmailExists(dto.getEmail())) {
            ArrayList<String> passList = passHandler.encryptPassword(dto.getPassHash());
            dto.setPassHash(passList.get(0));
            dto.setSalt(passList.get(1));
            return dao.createUser(dto);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email already exists");
    }


}
