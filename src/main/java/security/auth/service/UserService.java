package security.auth.service;

import security.auth.entity.User;
import security.auth.entity.VerificationToken;
import security.auth.model.UserModel;

public interface UserService {
    User registeruser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateNewverificationToken(String oldToken);
}
