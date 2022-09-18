package security.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.auth.entity.PasswordResetToken;


@Repository
public interface PasswordResetTokenRepository  extends JpaRepository<PasswordResetToken ,Long> {

     public static PasswordResetToken findByToken(String token){
         return  PasswordResetTokenRepository.findByToken(token);
     }
}
