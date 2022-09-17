package security.auth.controller;


import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import security.auth.entity.User;
import security.auth.entity.VerificationToken;
import security.auth.event.RegistrationCompleteEvent;
import security.auth.model.UserModel;
import security.auth.repository.UserRepository;
import security.auth.service.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public  String registerUser(@RequestBody UserModel userModel , final HttpServletRequest request){



        //using the entity we then create a field in the table
        User user = userService.registeruser(userModel);

        userRepository.save(user);

        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                appplicationUrl(request )
        ));

        return "Success";
    }
    @GetMapping("/verifyRegistration")
    public  String verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return  "user Verifies Successfully";
        }
        return "Bad user";
    }

    @GetMapping("/resendVerification")
    public String resendVerification(@RequestParam("token") String oldToken,
                                     HttpServletRequest request
                    ){
        VerificationToken verificationToken = userService.generateNewverificationToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationTokenMail(user,appplicationUrl(request) , verificationToken);
        
        return "verification link sent";

    }

    private void resendVerificationTokenMail(User user, String appplicationUrl,VerificationToken token) {


        String url  = appplicationUrl
                +"/verifyRegistration?token="
                +token;

        //send emailVerification
        log.info("Click to verify your account  :{}" + url);
    }


    private String appplicationUrl(HttpServletRequest request) {

        return "http://"+
                request.getServerName()+
                ":"+
                request.getServerPort() +
                request.getContextPath();

    }
}
