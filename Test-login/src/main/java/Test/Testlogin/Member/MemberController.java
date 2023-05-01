package Test.Testlogin.Member;

import Test.Testlogin.Form.ResponseForm;
import Test.Testlogin.Dto.MemberLoginDto;
import Test.Testlogin.Dto.MemberSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseForm login(@RequestBody MemberLoginDto memberLoginDto){
        return memberService.login(memberLoginDto);
    }

    @PostMapping("/signup")
    public String signup(@RequestBody MemberSignUpDto memberSignUpDto){
        return memberService.signUp(memberSignUpDto);
    }
}
