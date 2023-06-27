package Test.Testlogin.Email;

import Test.Testlogin.Email.emailCertification.CertDto.CertDto;
import Test.Testlogin.Email.emailCertification.EmailCertificationEntity;
import Test.Testlogin.Email.emailCertification.EmailCertificationRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class EmailUtilsImpl implements EmailUtils{
    private final JavaMailSender sender;
    private final EmailCertificationRepositoryCustom emailCertificationRepositoryCustom;

    @Override
    public Map<String, Object> sendMail(String emailAddress, String subject, String body) {
        Map<String, Object> result = new HashMap<String, Object>();
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try{
            helper.setTo(emailAddress);
            helper.setSubject(subject);
            helper.setText(body, true);
            result.put("resultCode", 200);
        } catch (MessagingException e){
            e.printStackTrace();
            result.put("resultCode",500);
        }
        sender.send(message);
        return result;
    }

    @Override
    public int createCertificatonNumber() {
        Random random = new Random();
        int certNumber = random.nextInt(999999 - 100000 + 1) + 100000;
        return certNumber;
    }

    @Override
    public boolean checkCertificationNumber(CertDto certDto) {
        EmailCertificationEntity entity = emailCertificationRepositoryCustom.findCertificateByEmail(certDto.getMemberId()).get();
        log.info("Checking certification number {}",entity);
        if (entity.getEmailCertificationNumber() == certDto.getCertNumber()) {
            return true;
        } else {
            return false;
        }
    }
}
