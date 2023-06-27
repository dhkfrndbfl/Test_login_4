package Test.Testlogin.Member;

import Test.Testlogin.Dto.MemberLoginDto;
import Test.Testlogin.Dto.MemberSignUpDto;
import Test.Testlogin.Email.emailCertification.CertDto.CertDto;
import Test.Testlogin.Form.ResponseForm;
import Test.Testlogin.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class MemberController {
    //memberService를 사용하기 위해서 final로 포함한다.
    private final MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;

    //인증번호 발송
    @PostMapping("/create")
    public ResponseForm sendCertNumber(@RequestBody CertDto certDto){
        return memberService.sendCertNumber(certDto);
    }

    //인증번호 인증
    @PostMapping("/check")
    public ResponseForm checkCoedNumber(@RequestBody CertDto certDto){
        return memberService.checkCertificationNumber(certDto);
    }

    //로그인
    @PostMapping("/login")
    public ResponseForm login(@RequestBody MemberLoginDto memberLoginDto){
        /**
         * 로그인
         * @RequestBody -> api로 부터 들어오는 요청을 Body에 담에서 받기 위해 사용한다.
         * MemberLoginDto -> Body에 담겨 들어올 데이터의 폼을 작성해 준다.
         *      - memberId : 회원 아이디데이터를 입력받을 변수
         *      - memberPwd : 회원 비밀번호 데이터를 입력받을 변수
         */
        return memberService.login(memberLoginDto);
    }

    //회원 가입
    @PostMapping("/signup")
    public String signup(@RequestBody MemberSignUpDto memberSignUpDto){
        /**
         * 회원 가입
         * @RequestBody -> api로 부터 들어오는 요청을 Body에 담에서 받기 위해 사용한다.
         * MemberSignUpDto -> Body에 담겨 들어올 데이터의 폼을 작성해 준다.
             * memberId :회원 아이디 데이터를 입력받을 변수
             * memberPwd : 회원 비밀번호 데이터를 입력받을 변수
             * memberName : 회원 이름 데이터를 입력받을 변수
             * memberAge : 회원 나이 데이터를 입력받을 변수
         */
        return memberService.signUp(memberSignUpDto);
    }

}

