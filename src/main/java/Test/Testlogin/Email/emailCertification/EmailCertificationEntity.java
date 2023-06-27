package Test.Testlogin.Email.emailCertification;

import Test.Testlogin.Member.MemberEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter

@Table(name = "email_cert")
public class EmailCertificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "email_cert_id")
    private Long emailCertificationId;

    @Column(name = "email_cert_email")
    private String emailCertificationEmail;

    @OneToOne(mappedBy =  "emailCertificationEntity")
    private MemberEntity memberEntity;

    @Column(name = "email_cert_number")
    int emailCertificationNumber;

    @Column(name = "email_cert_enabled")
    boolean emailCertificationEnabled;

    @Column(name = "email_cert_created_at")
    LocalDateTime emailCertCreatedAt;

    @Column(name = "email_cert_updated_at")
    LocalDateTime emailCertUpdatedAt;
}
