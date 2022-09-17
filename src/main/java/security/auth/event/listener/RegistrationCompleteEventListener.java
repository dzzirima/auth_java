package security.auth.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import security.auth.entity.User;
import security.auth.event.RegistrationCompleteEvent;
import security.auth.service.UserService;

import java.util.UUID;

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {


    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        //create the verification Token for the user with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        //send mail to User
        userService.saveVerificationTokenForUser(token , user);

        String url  = event.getApplicationUrl()
                    +"verifyRegistration?token="
                    +token;

        //send emailVerification
        System.out.println("Click to verify your account  :");




    }
}
