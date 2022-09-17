package security.auth.controller;


import com.sun.net.httpserver.HttpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import security.auth.entity.User;
import security.auth.event.RegistrationCompleteEvent;
import security.auth.model.UserModel;
import security.auth.service.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public  String registerUser(@RequestBody UserModel userModel , final HttpServletRequest request){



        //using the entity we then create a field in the table
        User user = userService.registeruser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                appplicationUrl(request )
        ));

        return "Success";
    }

    private String appplicationUrl(HttpServletRequest request) {

        return "http://"+
                request.getServerName()+
                ":"+
                request.getServerPort() +
                request.getContextPath();

    }
}
