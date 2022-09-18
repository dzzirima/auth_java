package security.auth.controller;


import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import security.auth.entity.User;
import security.auth.entity.VerificationToken;
import security.auth.event.RegistrationCompleteEvent;
import security.auth.model.PasswordModel;
import security.auth.model.UserModel;
import security.auth.repository.UserRepository;
import security.auth.service.UserService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

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

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel ,HttpServletRequest request){

        User user= userService.findUserByEmail(passwordModel.getEmail());

        String url = "";
        if(user!=null){
            String token = UUID.randomUUID().toString();

            userService.createPasswordResetTokenForUser(user,token);
            url = passwordResetTokenMail(user,appplicationUrl(request),token);
        }
        return url;

    }

    private String passwordResetTokenMail(User user, String appplicationUrl, String token) {
        String url  = appplicationUrl
                +"/savePassword?token="
                +token;

        //send emailVerification
        log.info("Click to Reset Your password :{}" + url);

        return  url;
    }

    @PostMapping("/savePassword")
    public  String savePassword(@RequestParam("token") String token ,
                                @RequestBody PasswordModel passwordModel){

        String result = userService.validatePasswordResetToken(token);

        if(!result.equalsIgnoreCase("valid")){
            return "Invalid Token";

        }

        Optional<User> user = userService.getUserPasswordResetToken(token);

        if(user.isPresent()){
            userService.changePassword(user.get() , passwordModel.getNewPassword());
            return "Password Reset Successfully";
        }else {
            return  "Invalid token";
        }


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
