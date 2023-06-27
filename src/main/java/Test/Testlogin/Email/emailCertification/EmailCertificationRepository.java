package Test.Testlogin.Email.emailCertification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCertificationRepository extends JpaRepository<EmailCertificationEntity, Long> {

}
