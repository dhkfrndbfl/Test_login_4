package Test.Testlogin.Member;

import Test.Testlogin.Dto.MemberLoginDto;
import Test.Testlogin.Dto.MemberSignUpDto;
import Test.Testlogin.Email.EmailUtils;
import Test.Testlogin.Email.EmailValidation;
import Test.Testlogin.Email.emailCertification.CertDto.CertDto;
import Test.Testlogin.Email.emailCertification.EmailCertificationEntity;
import Test.Testlogin.Email.emailCertification.EmailCertificationImplCustom;
import Test.Testlogin.Email.emailCertification.EmailCertificationRepository;
import Test.Testlogin.Form.ResponseForm;
import Test.Testlogin.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor            //final이 붙은 필드의 생성자를 자동으로 생성해주는 어노테이션
@Service                            //service를 지정해주는 어노테이션
@Transactional                      //데이터 추가, 갱신, 삭제 등으로 이루어진 작업을 처리하던 중 오류가 발생했을 때 모든 작업들을 원상태로 되돌릴 수 있다.
@Slf4j                              //로그 라이브러리를 사용하기 위해 사용
@PropertySource("classpath:application.properties")

public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailUtils emailUtils;
    private final EmailCertificationImplCustom emailCertificationImplCustom;
    private final EmailCertificationRepository emailCertificationRepository;

    /**
     * 회원가입
     * @param memberSignUpDto
     * @return
     */
    public String signUp(MemberSignUpDto memberSignUpDto){
        //이메일 형식 검사
        if (EmailValidation.isValid(memberSignUpDto.getMemberId())) {
            //이메일 형식 일경우

            Optional<MemberEntity> member = memberRepository.findByMemberId(memberSignUpDto.getMemberId());
            //DB에 입력받은 이메일 정보가 존재하는지 확인

            if (member.isPresent()){    //일치하는 정보가 존재한다면 중복회원
                return "중복된 회원입니다.";
            }else {
                //일치하는 정보가 없다면 신규 회원 가입
                MemberEntity memberEntity = new MemberEntity();     //새로운 회원 객체 만듬
                memberEntity.signUpDtoEntity(memberSignUpDto);    //dto 객체를 entity 객체로 변환
                memberRepository.save(memberEntity);                //entity 정보를 DB에 저장한다.
                return "회원 가입 성공";
            }
        } else {
            return "이메일 형식의 아이디를 입력해 주세요";
        }
    }
    /**
     * 로그인
     * @param dto
     * @return
     */
    public ResponseForm login(MemberLoginDto dto){
        //ResponseForm을 통해서 Response에 대한 형식을 정형화 해서 진행
        ResponseForm responseForm = new ResponseForm();
        //이메일 형식 검사
        if (!EmailValidation.isValid(dto.getMemberId())){
            //올바른 형식이 아니라면
            responseForm.setHttpStatus(HttpStatus.BAD_REQUEST);
            responseForm.setResult("");
            responseForm.setComment("잘못된 요청입니다.");
        }else { //이메일 형식

            Optional<MemberEntity> member = memberRepository.findByMemberId(dto.getMemberId());
            // DB 조회 -> select * from member_manage where member_id = 'dto.getMemberId()';

            //회원 아이디 찾기
            if (member.isEmpty()){              //조회한 데이터가 비어있다면 -> 일치하는 회원 정보가 없다.
                responseForm.setHttpStatus(HttpStatus.BAD_REQUEST);
                responseForm.setResult("");
                responseForm.setComment("일치하는 회원정보가 없습니다.");
            }else {
                //회원 정보가 존재 하는경우
                //요청이 들어온 비밀번호가 DB의 비밀번호와 일치하는지 확인해야함
                if (passwordCheck(dto.getMemberPwd(),member.get().memberPwd)) {
                    //비밀번호가 일치할경우
                    String token = jwtTokenProvider.createToken(member.get().memberPwd);       //토큰 생성    토큰을 사용해서 회원인증을 한다   토큰의 들어갈 사용자 정보는 미정이다.(데이터베이스에 생성되는 시리얼 넘버로 구상 중.)
                    member.get().setMemberToken(token);                                         //토큰저장     토큰을 DB에 저장해서 관리한다
                    responseForm.setHttpStatus(HttpStatus.OK);                                  //httpstatus를 ok로 설정하여 폼에 담는다
                    responseForm.setResult(member.get().getMemberName());                       //회원의 이름을 반환한다
                    responseForm.setComment("로그인을 환영합니다.");
                    responseForm.setAccessToken(token);                                         //회원의 인증을 담당할 토큰을 생성해서 응답한다. 프론트에 인증이 된 회원인지, 어떤 사용자인지 알려주기 위해
                } else {
                    responseForm.setHttpStatus(HttpStatus.BAD_REQUEST);
                    responseForm.setResult("");
                    responseForm.setComment("비밀번호가 일치하지 않습니다.");
                }
            }
        }
        return responseForm;
    }

/*    @Override       //security 때문
    public MemberEntity loadUserByUsername(String username){
        //MemberEntity 정보 조회
        Optional<MemberEntity> memberEntity = memberRepository.findByMemberId(username);


        if(memberEntity.isPresent()) {      //해당하는 값이 있다면
            MemberEntity memberEntityValue = memberEntity.get();

            MemberEntity member = MemberEntity.builder()
                    .memberSn(memberEntityValue.getMemberSn())
                    .memberId(memberEntityValue.getMemberId())
                    .memberPwd(memberEntityValue.getMemberPwd())
                    .memberName(memberEntityValue.getMemberName())
                    .memberAge(memberEntityValue.getMemberAge())
                    .memberToken(memberEntityValue.getMemberToken())
                    .build();

            log.info("member : {}", member);
            return member;
        }
        return null;
    }*/

    public boolean passwordCheck(String dtoPwd,String memberPwd) {              // //DB에 있는 패스워드랑 입력한 패스워드가 맞는지 확인
        return memberPwd.equals(dtoPwd);
    }

    /**
     * 인증번호 발송 서비스
     * @param certDto
     * @return
     */
    public ResponseForm sendCertNumber(CertDto certDto) {
        ResponseForm responseForm = new ResponseForm();
        try{
            int certCode = emailUtils.createCertificatonNumber();
            EmailCertificationEntity emailCertificationEntity = new EmailCertificationEntity();
            emailCertificationEntity.setEmailCertificationEmail(certDto.getMemberId());
            emailCertificationEntity.setEmailCertificationNumber(certCode);
            emailCertificationEntity.setEmailCertCreatedAt(LocalDateTime.now());
            emailCertificationEntity.setEmailCertificationEnabled(false);
            emailCertificationRepository.save(emailCertificationEntity);
            String certEmailForm = certEmailFormat1 + String.valueOf(certCode) + certEmailFormat2;
            emailUtils.sendMail(certDto.getMemberId(), "이메일 인증 번호입니다.", htmlform1+certEmailForm+htmlform2);
            responseForm.setHttpStatus(HttpStatus.OK);
            responseForm.setComment("인증번호가 발송되었습니다. 인증을 진행해주세요.");
        } catch (Exception e){
            responseForm.setHttpStatus(HttpStatus.BAD_REQUEST);
            responseForm.setComment("인증메일 발송에 실패하였습니다. 다시 시도해주세요");
        }
        return responseForm;
    }

    /**
     * 인증 번호 확인
     * @param certDto
     * @return
     */
    public ResponseForm checkCertificationNumber(CertDto certDto){
        ResponseForm result = new ResponseForm();
        if(emailUtils.checkCertificationNumber(certDto)){
            emailCertificationImplCustom.accessEmailCertification(certDto.getMemberId());
            result.setHttpStatus(HttpStatus.OK);
            result.setResult(true);
            result.setComment("인증에 성공하셨습니다.");
        } else {
            result.setHttpStatus(HttpStatus.BAD_REQUEST);
            result.setResult(false);
            result.setComment("인증번호가 올바르지 않습니다.");
        }
        return result;
    }

    public static String certEmailFormat1 = "";
    public static String certEmailFormat2 = "";

    private static final String htmlform1= "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
            "    <title>Demystifying Email Design</title>\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
            "</head>\n" +
            "<body style=\"margin: 0; padding: 0;\">\n" +
            "<table align=\"center\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"50%\">\n" +
            "    <tr>\n" +
            "        <td align=\"center\" bgcolor=\"#70bbd9\" style=\"width: 600px; height:300px;\">\n" +
            "            <img src=\"./img/c&pimg.png\" style=\"width:100%; height: 100%\" />\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td bgcolor=\"#ffffff\" style=\"padding: 40px 30px 40px 30px;\">\n" +
            "            <table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
            "                <tr>\n" +
            "                    <td width=\"260\" valign=\"top\">\n" +
            "                        <table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
            "                            <tr>\n" +
            "                                <td alt=\"\" width=\"100%\" height=\"140\" style=\"display: block;\">\n" +
            "                                    아래의 인증코드를 입력하여 본인인증을 완료해 주세요\n" +
            "                                </td>\n" +
            "                            </tr>\n" +
            "                            <tr>\n" +
            "                                <td style=\"padding: 25px 0 0 0;\">";

private static final String htmlform2 = "</td>\n" +
        "                            </tr>\n" +
        "                        </table>\n" +
        "                    </td>\n" +
        "                </tr>\n" +
        "            </table>\n" +
        "        </td>\n" +
        "    </tr>\n" +
        "    <tr>\n" +
        "        <td bgcolor=\"#ee4c50\" style=\"padding: 30px 30px 30px 30px;\">\n" +
        "            <table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
        "                <tr></tr>\n" +
        "                    <td align=\"right\">\n" +
        "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
        "                            <tr>\n" +
        "                                <td>\n" +
        "                                    <a href=\"http://www.twitter.com/\">\n" +
        "                                        <img src=\"images/tw.gif\" alt=\"Twitter\" width=\"38\" height=\"38\" style=\"display: block;\" border=\"0\" />\n" +
        "                                    </a>\n" +
        "                                </td>\n" +
        "                                <td style=\"font-size: 0; line-height: 0;\" width=\"20\">&nbsp;</td>\n" +
        "                            </tr>\n" +
        "                        </table>\n" +
        "                    </td>\n" +
        "                    <td width=\"75%\">\n" +
        "                        &reg; Someone, somewhere 2013<br/>\n" +
        "                        Unsubscribe to this newsletter instantly\n" +
        "                    </td>\n" +
        "                    </td>\n" +
        "                </tr>\n" +
        "            </table>\n" +
        "        </td>\n" +
        "\n" +
        "    </tr>\n" +
        "</table>\n" +
        "</body>\n" +
        "</html>";



}
    


