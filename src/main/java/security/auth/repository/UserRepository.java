package security.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.auth.entity.User;

public interface UserRepository  extends JpaRepository<User , Long> {
}
