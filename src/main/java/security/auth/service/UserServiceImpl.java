package security.auth.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.auth.entity.User;
import security.auth.entity.VerificationToken;
import security.auth.model.UserModel;
import security.auth.repository.UserRepository;
import security.auth.config.WebSecurityConfig;
import security.auth.repository.VerificationTokenRepository;

@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository ;
    @Override
    public User registeruser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword( passwordEncoder.encode(  userModel.getPassword()));

        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {

        VerificationToken verificationToken = new VerificationToken(user,token);

        verificationTokenRepository.save(verificationToken);



    }
}
