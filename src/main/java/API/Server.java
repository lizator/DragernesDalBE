package API;

import dal.MainDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class,args);
    }
}

@RestController
class MainController{
    MainDAO dao = new MainDAO();
    @RequestMapping("/main/tableupdate/{tableName}")
    String tableUpdate(@PathVariable String tableName){
        return dao.getLastTimeTableModified(tableName);
    }

}

//Eksempel på brug af Rest API i Spring
@RestController
class GreetingsController{
    @RequestMapping("/hello/{name}")
    String hello(@PathVariable String name){
        return "Hello " + StringUtils.capitalize(name) + "!";
    }
}

