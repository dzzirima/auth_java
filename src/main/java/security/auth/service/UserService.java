package security.auth.service;

import security.auth.entity.User;
import security.auth.entity.VerificationToken;
import security.auth.model.UserModel;

import java.util.Optional;

public interface UserService {
    User registeruser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateNewverificationToken(String oldToken);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserPasswordResetToken(String token);

    void changePassword(User user, String newPassword);
}
