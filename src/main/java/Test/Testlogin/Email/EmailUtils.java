package Test.Testlogin.Email;

import Test.Testlogin.Email.emailCertification.CertDto.CertDto;

import java.util.Map;

public interface EmailUtils {
    Map<String, Object> sendMail(String emailAddress, String subject, String body);

    int createCertificatonNumber();

    boolean checkCertificationNumber(CertDto certDto);
}

