package security.auth.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.auth.entity.PasswordResetToken;
import security.auth.entity.User;
import security.auth.entity.VerificationToken;
import security.auth.model.UserModel;
import security.auth.repository.PasswordResetTokenRepository;
import security.auth.repository.UserRepository;
import security.auth.config.WebSecurityConfig;
import security.auth.repository.VerificationTokenRepository;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository ;
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
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

    @Override
    public String validateVerificationToken(String token) {

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null){
            return "invalid token";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if(verificationToken.getExpirationTime().getTime() -cal.getTime().getTime()  <=0){
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewverificationToken(String oldToken) {
        VerificationToken verificationToken  = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;


    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);


    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = PasswordResetTokenRepository.findByToken(token);
        if(passwordResetToken == null){
            return "invalid token";
        }

        User user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();
        if(passwordResetToken.getExpirationTime().getTime() -cal.getTime().getTime()  <=0){
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }

        return "valid";
    }

    @Override
    public Optional<User> getUserPasswordResetToken(String token) {
        return Optional.ofNullable(PasswordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

    }
}
