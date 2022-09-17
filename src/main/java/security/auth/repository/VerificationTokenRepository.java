package security.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.auth.entity.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<security.auth.entity.VerificationToken,Long> {

    VerificationToken findByToken(String token);
}
