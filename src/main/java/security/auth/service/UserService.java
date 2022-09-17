package security.auth.service;

import security.auth.entity.User;
import security.auth.model.UserModel;

public interface UserService {
    User registeruser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);
}
