package API;

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


//Eksempel på brug af Rest API i Spring
@RestController
class GreetingsController{
    @RequestMapping("/hello/{name}")
    String hello(@PathVariable String name){
        return "Hello " + StringUtils.capitalize(name) + "!";
    }
}

