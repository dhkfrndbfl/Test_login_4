package Test.Testlogin.Email.emailCertification;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmailCertificationImplCustom  implements EmailCertificationRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<EmailCertificationEntity> findEmailCertById(Long id) {
        QEmailCertificationEntity emailCert;
        emailCert = QEmailCertificationEntity.emailCertificationEntity;
        EmailCertificationEntity emailCertEntity = jpaQueryFactory.selectFrom(emailCert)
                .where(emailCert.emailCertificationId.eq(id)).fetchOne();

        return Optional.ofNullable(emailCertEntity);
    }

    @Override
    public Optional<EmailCertificationEntity> findCertificateByEmail(String email) {
        QEmailCertificationEntity emailCert = QEmailCertificationEntity.emailCertificationEntity;
        EmailCertificationEntity emailCertEntity = jpaQueryFactory.selectFrom(emailCert)
                .where(emailCert.emailCertificationEmail.eq(email)).fetchOne();
        return Optional.ofNullable(emailCertEntity);
    }

    @Override
    public Long accessEmailCertification(String email) {
        QEmailCertificationEntity emailCert = QEmailCertificationEntity.emailCertificationEntity;
        Long emailCertEntityId = jpaQueryFactory.update(emailCert)
                .set(emailCert.emailCertificationEnabled, true)
                .where(emailCert.emailCertificationEmail.eq(email))
                .execute();
        return emailCertEntityId;
    }

}
